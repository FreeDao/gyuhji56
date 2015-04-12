package rays.web.salon.controllers;

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
	final int pageSize = 5;
	final int statusValue[] = new int[] { SourceDetectVo.isIgnored,
			SourceDetectVo.isWatching, SourceDetectVo.isAuditing };

	@RequestMapping("/admin/control/auditNextHostV2")
	public Object auditNextHost() {
		PageScoreDao imp = new PageScoreDaoImpl();
		List<HostDetectedVo> result = imp.selectHostDetectedList(0, 1);
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
			@RequestParam(required = false, defaultValue = "") String filePath)
			throws Exception {
		HashMap<String, Object> msg = new HashMap<String, Object>();
		PageScoreDao impl = new PageScoreDaoImpl();
		List<PageDetectedVo> vos = impl.selectHostDetectedPages(host);
		boolean isWatching = false;
		boolean audited = false;
		msg.put("host", host);
		msg.put("watching", isWatching);
		msg.put("audited", audited);
		msg.put("vos", vos);
		msg.put("curPage", getPageData(filePath));

		return new ModelAndView("metronics.detectedHostV2", "msg", msg);
	}

	private DecodeSynCombin getPageData(String mdirPath) throws Exception {
		MDir mdir;
		String fileName[] = MFile.parseUri(mdirPath);
		SiteMDirReader reader = new SiteMDirReader(mdir);
		return reader.getFile(fileName[1]);

	}

	@RequestMapping("/admin/control/detectedListV2")
	public ModelAndView DetectedHostsList(
			@RequestParam(required = false, defaultValue = "1") int pageIndex,
			@RequestParam(defaultValue = "1") int sortBy) {
		Map<String, Object> msg = this.newMsgMap();
		if (pageIndex < 1) {
			pageIndex = 1;

		}
		int offset = (pageIndex - 1) * pageSize;

		PageScoreDao imp = new PageScoreDaoImpl();
		List<HostDetectedVo> result = imp.selectHostDetectedList(offset,
				pageSize);
		int totalUnAudit = 345678;

		PageSplit pageSplit = new PageSplit(totalUnAudit, pageSize, pageIndex);
		msg.put("list", result);
		msg.put("total", totalUnAudit);
		msg.put("pageSplit", pageSplit);
		return new ModelAndView("metronics.detectedListV2", "msg", msg);
	}
}
