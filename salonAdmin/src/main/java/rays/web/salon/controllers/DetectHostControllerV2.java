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

import rays.web.rays.dao.mybatis.PageScoreDao;
import rays.web.rays.dao.mybatis.imp.PageScoreDaoImpl;
import rays.web.rays.dao.mybatis.imp.WebScanDaoImpl;
import rays.web.rays.vo.HostDetectedVo;
import rays.web.rays.vo.PageDetectedVo;
import rays.web.rays.vo.SourceDetectVo;

import com.bmtech.spider.core.util.SiteMDirReader;
import com.bmtech.spider.core.util.SynCombin.DecodeSynCombin;
import com.bmtech.utils.PageSplit;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;

@Controller
public class DetectHostControllerV2 extends AbstractController {
	final int pageSize = 10;
	final int statusValue[] = new int[] { SourceDetectVo.isIgnored,
			SourceDetectVo.isWatching, SourceDetectVo.isAuditing };

	@RequestMapping("/admin/control/auditNextHostV2")
	public Object auditNextHost() {
		PageScoreDao imp = new PageScoreDaoImpl();
		List<HostDetectedVo> result = imp.selectHostDetectedList(0, 10,
				PageDetectedVo.STATUS_UNAUDIT);
		System.out.println(result);
		if (result.size() > 0) {
			HostDetectedVo vo = result.get(0);
			return new RedirectView("detectedHostV2.html?host=" + vo.getHost());
		} else {
			Map<String, Object> msg = newMsgMap();
			this.setRetMsg(msg, 404, "no more need audit");
			return jsonView(msg);
		}
	}

	@RequestMapping("/admin/control/setHostAuditStatusV2")
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

	@RequestMapping("/admin/control/setPageAuditStatusV2")
	public ModelAndView detectedPageStatusModify(
			@RequestParam(required = true) String pageId_p,
			@RequestParam(required = true) String status_p) {
		int pageId = this.parseInt(pageId_p, 0);
		int status = this.parseInt(status_p, 0);
		System.out.println("status:" + status + ",pageId=" + pageId);
		PageScoreDao imp = new PageScoreDaoImpl();
		imp.setPageStatus(pageId, status);
		Map<String, Object> msg = this.newMsgMap();

		setRetMsg(msg, 200, "OK");
		System.out.println("msg:" + msg);
		return jsonView(msg);
	}

	private int statusCode(String status) {
		status = status.trim();

		try {
			return Integer.parseInt(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@RequestMapping("/admin/control/detectedHostV2")
	public ModelAndView detectedHost(
			@RequestParam(required = true, defaultValue = "") String host,
			@RequestParam(required = false) String status_p) throws Exception {
		int status = this.parseInt(status_p, PageDetectedVo.STATUS_ALL);
		System.out.println("status nows " + status);
		HashMap<String, Object> msg = new HashMap<String, Object>();
		PageScoreDao impl = new PageScoreDaoImpl();
		List<PageDetectedVo> vos = impl.selectHostDetectedPages(host, status);
		msg.put("host", host);
		msg.put("vos", vos);
		System.out.println(vos);

		return new ModelAndView("metronics.detectedHostV2", "msg", msg);
	}

	@RequestMapping("/admin/control/detectedPageList")
	public ModelAndView detectedPageList(
			@RequestParam(required = false) String status_p,
			@RequestParam(required = false) String pageIndex_p)
			throws Exception {
		int status = this.parseInt(status_p, PageDetectedVo.STATUS_ALL);
		int pageIndex = this.parseInt(pageIndex_p, 1);

		PageScoreDao impl = new PageScoreDaoImpl();

		int totalItems = impl.selectPageDetectedNum(status);
		PageSplit pageSplit = new PageSplit(totalItems, pageSize, pageIndex, 5);

		List<PageDetectedVo> vos = impl.selectDetectedPages(status,
				pageSplit.getItemStartIndex(), pageSize);

		HashMap<String, Object> msg = new HashMap<String, Object>();
		msg.put("total", totalItems);
		msg.put("pageSplit", pageSplit);
		msg.put("vos", vos);
		msg.put("statusName", PageDetectedVo.satusName(status));
		msg.put("status", status);
		System.out.println(vos);

		return new ModelAndView("metronics.detectedPageList", "msg", msg);
	}

	private DecodeSynCombin getPageData(String mdirPath) throws Exception {
		System.out.println(mdirPath);
		String fileName[] = MFile.parseUri(mdirPath);
		File mdirFile = new File(mdirPath);
		MDir mdir = MDir.open(mdirFile);
		SiteMDirReader reader = new SiteMDirReader(mdir);
		return reader.getFile(fileName[1]);

	}

	@RequestMapping("/admin/control/getDetectedPageV2")
	public ModelAndView getDetectedPageV2(
			@RequestParam(required = false, defaultValue = "0") int pageId) {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		PageScoreDao imp = new PageScoreDaoImpl();
		PageDetectedVo vo = null;
		if (pageId > 0) {
			vo = imp.selectDetectedPage(pageId);
		}
		if (vo == null) {
			setRetMsg(msg, 404, "没有找到页面， pageId=" + pageId);
		} else {
			setRetMsg(msg, 200, "OK");
			msg.put("PageDetectedVo", vo);
		}
		return new ModelAndView("jsonView", "msg", msg);
	}

	@RequestMapping("/admin/control/detectedListV2")
	public ModelAndView detectedHostsList(
			@RequestParam(required = false, defaultValue = "1") int pageIndex,
			@RequestParam(defaultValue = "1") int sortBy,
			@RequestParam(required = false) String statusPara) {
		int status = this.parseInt(statusPara, PageDetectedVo.STATUS_ALL);
		Map<String, Object> msg = this.newMsgMap();
		if (pageIndex < 1) {
			pageIndex = 1;
		}
		int offset = (pageIndex - 1) * pageSize;

		PageScoreDao imp = new PageScoreDaoImpl();
		List<HostDetectedVo> result = imp.selectHostDetectedList(offset,
				pageSize, status);
		System.out.println("status now " + status);

		System.out.println(result);

		int totalUnAudit = imp.selectHostDetectedHost(status);
		System.out.println(result);
		PageSplit pageSplit = new PageSplit(totalUnAudit, pageSize, pageIndex);
		msg.put("list", result);
		msg.put("total", totalUnAudit);
		msg.put("pageSplit", pageSplit);

		return new ModelAndView("metronics.detectedListV2", "msg", msg);
	}
}
