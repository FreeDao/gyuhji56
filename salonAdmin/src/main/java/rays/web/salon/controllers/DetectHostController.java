package rays.web.salon.controllers;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import rays.web.rays.dao.mybatis.imp.WebScanDaoImpl;
import rays.web.rays.vo.DetectedPage;
import rays.web.rays.vo.SourceDetectVo;

import com.bmtech.utils.PageSplit;

@Controller
public class DetectHostController extends AbstractController {
	final int pageSize = 5;
	final String statusName[] = new String[] { "ignore", "watching", "auditing" };
	final int statusValue[] = new int[] { SourceDetectVo.isIgnored,
			SourceDetectVo.isWatching, SourceDetectVo.isAuditing };

	@RequestMapping("/admin/control/auditNextHost")
	public Object auditNextHost() {
		WebScanDaoImpl imp = new WebScanDaoImpl();
		List<SourceDetectVo> result = imp.selectSourceDetectedList(0, 1, true);
		if (result.size() > 0) {
			SourceDetectVo vo = result.get(0);
			return new RedirectView("detectedHost.html?host=" + vo.getHost());
		} else {
			Map<String, Object> msg = newMsgMap();
			this.setRetMsg(msg, 404, "no more need audit");
			return jsonView(msg);
		}
	}

	@RequestMapping("/admin/control/setHostAuditStatus")
	public ModelAndView detectedHostModify(
			@RequestParam(required = true, defaultValue = "") String host,
			@RequestParam(required = true, defaultValue = "") String status) {
		Map<String, Object> msg = this.newMsgMap();
		msg.put("host", host);
		host = host.trim().toLowerCase();
		status = status.trim().toLowerCase();
		if (host.length() == 0 || status.length() == 0) {
			setRetMsg(msg, 302, String.format("invalid!  '%s' to status '%s'",
					host, status));
			return new ModelAndView("jsonView", "msg", msg);
		}
		int sCode = statusCode(status);
		if (sCode == -1) {
			setRetMsg(msg, 403, String.format("invalid!  '%s' to status '%s'",
					host, status));
			return new ModelAndView("jsonView", "msg", msg);
		}
		WebScanDaoImpl impl = new WebScanDaoImpl();
		impl.setHostStatus(host, sCode);
		setRetMsg(msg, 200, "OK");
		System.out.println("msg:" + msg);
		return jsonView(msg);
	}

	private int statusCode(String status) {
		status = status.trim();

		for (int x = 0; x < statusName.length; x++) {
			if (statusName[x].equalsIgnoreCase(status)) {
				return statusValue[x];
			}
		}
		return -1;
	}

	@RequestMapping("/admin/control/detectedHost")
	public ModelAndView detectedHost(
			@RequestParam(required = true, defaultValue = "") String host,
			@RequestParam(required = false, defaultValue = "") String filePath) {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		WebScanDaoImpl impl = new WebScanDaoImpl();
		List<SourceDetectVo> vos = impl.getDetectedHost(host);
		List<DetectedPage> pages = impl.getDetectedPages(vos);
		boolean isWatching = false;
		boolean audited = false;
		if (vos.size() > 0) {
			isWatching = vos.get(0).isWatching();
			audited = !vos.get(0).isAuditing();
		}
		msg.put("host", host);
		msg.put("watching", isWatching);
		msg.put("audited", audited);
		msg.put("vos", vos);
		msg.put("pages", pages);
		msg.put("level_0", sumLevel(vos, 0));
		msg.put("level_1", sumLevel(vos, 1));
		msg.put("level_2", sumLevel(vos, 2));
		msg.put("level_3", sumLevel(vos, 3));
		msg.put("curPage", curPageIndex(pages, filePath));

		return new ModelAndView("metronics.detectedHost", "msg", msg);
	}

	private DetectedPage curPageIndex(List<DetectedPage> pages, String filePath) {
		if (pages.size() == 0) {
			return null;
		}
		if (filePath.length() == 0) {
			return pages.get(0);
		}
		File file = new File(filePath);
		for (DetectedPage p : pages) {
			if (p.getFile().equals(file)) {
				return p;
			}
		}
		return null;
	}

	private int sumLevel(List<SourceDetectVo> vos, int level) {
		int sum = 0;
		for (SourceDetectVo vo : vos) {
			if (level == 0) {
				sum += vo.getScan_num();
			} else if (level == 1) {
				sum += vo.getLevel_1_count();
			} else if (level == 2) {
				sum += vo.getLevel_2_count();
			} else if (level == 3) {
				sum += vo.getLevel_3_count();
			}
		}
		return sum;
	}

	@RequestMapping("/admin/control/detectedList")
	public ModelAndView DetectedHostsList(
			@RequestParam(required = false, defaultValue = "1") int pageIndex,
			@RequestParam(defaultValue = "1") int sortBy) {
		Map<String, Object> msg = this.newMsgMap();
		if (pageIndex < 1) {
			pageIndex = 1;

		}
		int offset = (pageIndex - 1) * pageSize;

		WebScanDaoImpl imp = new WebScanDaoImpl();
		List<SourceDetectVo> result = imp.selectSourceDetectedList(offset,
				pageSize, true);
		int totalUnAudit = imp.selectTotalUnAudit();

		PageSplit pageSplit = new PageSplit(totalUnAudit, pageSize, pageIndex);
		msg.put("list", result);
		msg.put("total", totalUnAudit);
		msg.put("pageSplit", pageSplit);
		return new ModelAndView("metronics.detectedList", "msg", msg);
	}
}
