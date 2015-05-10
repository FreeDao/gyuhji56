package rays.web.salon.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import rays.web.rays.dao.mybatis.PageScoreDao;
import rays.web.rays.dao.mybatis.imp.PageScoreDaoImpl;
import rays.web.rays.dao.mybatis.imp.WebScanDaoImpl;
import rays.web.rays.vo.AdminUserVo;
import rays.web.rays.vo.HostDetectedVo;
import rays.web.rays.vo.PageDetectedVo;
import rays.web.rays.vo.SourceDetectVo;
import rays.web.rays.vo.UserLoginVo;

import com.bmtech.spider.ext.scorer.scorers.MultiScorer;
import com.bmtech.utils.PageSplit;
import com.bmtech.utils.io.TchFileTool;
import com.bmtech.utils.log.LogHelper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DetectHostControllerV2 extends AbstractController {
	static final LogHelper log = new LogHelper("DetectHostControllerV2");
	static final String mockMdirPath;
	static {
		String mockMdirPathx = TchFileTool.get("./config", "mockMdirPath");
		System.out.println(new File("./config").getAbsolutePath());
		log.warn("mockMdirPath %s", mockMdirPathx);
		if (mockMdirPathx != null && mockMdirPathx.length() == 0) {
			mockMdirPath = null;
		} else {
			mockMdirPath = mockMdirPathx;
		}
	}

	private final String loginKey = "userLogin";
	private final String loginRedirectKey = "loginRedirect";

	final int pageSize = 10;
	final int statusValue[] = new int[] { SourceDetectVo.isIgnored,
			SourceDetectVo.isWatching, SourceDetectVo.isAuditing };

	@RequestMapping("/admin/control/auditNextHostV2")
	public Object auditNextHost(
			@CookieValue(value = "u", defaultValue = "") String cookieId) {
		Map<String, Object> msg = checkLogin(cookieId);
		if (!isLogin(msg)) {
			return msg.get(this.loginRedirectKey);
		}

		PageScoreDao imp = new PageScoreDaoImpl();
		List<HostDetectedVo> result = imp.selectHostDetectedList(0, 10,
				PageDetectedVo.STATUS_UNAUDIT);
		if (result.size() > 0) {
			HostDetectedVo vo = result.get(0);
			return new RedirectView("detectedHostV2.html?host=" + vo.getHost());
		} else {
			this.setRetMsg(msg, 404, "no more need audit");
			return jsonView(msg);
		}
	}

	@RequestMapping("/admin/control/setHostAuditStatusV2")
	public Object detectedHostModify(
			@RequestParam(required = true, defaultValue = "") String host,
			@RequestParam(required = true, defaultValue = "") String status,
			@CookieValue(value = "u", defaultValue = "") String cookieId) {
		Map<String, Object> msg = checkLogin(cookieId);
		if (!isLogin(msg)) {
			return msg.get(this.loginRedirectKey);
		}

		msg.put("host", host);
		host = host.trim().toLowerCase();
		status = status.trim().toLowerCase();
		if (host.length() == 0 || status.length() == 0) {
			setRetMsg(msg, 302, String.format("invalid!  '%s' to status '%s'",
					host, status));
			return new ModelAndView("jsonView", "msg", msg);
		}
		int sCode = parseInt(status, -1);
		if (sCode == -1) {
			setRetMsg(msg, 403, String.format("invalid!  '%s' to status '%s'",
					host, status));
			return new ModelAndView("jsonView", "msg", msg);
		}
		WebScanDaoImpl impl = new WebScanDaoImpl();
		impl.setHostStatus(host, sCode);
		setRetMsg(msg, 200, "OK");
		return jsonView(msg);
	}

	@RequestMapping("/admin/control/setPageAuditStatusV2")
	public Object detectedPageStatusModify(
			@RequestParam(required = true) String pageId_p,
			@RequestParam(required = true) String status_p,
			@CookieValue(value = "u", defaultValue = "") String cookieId) {
		Map<String, Object> msg = checkLogin(cookieId);
		if (!isLogin(msg)) {
			return msg.get(this.loginRedirectKey);
		}

		int pageId = this.parseInt(pageId_p, 0);
		int status = this.parseInt(status_p, 0);
		log.info("status:%s,pageId=", status, pageId);
		PageScoreDao imp = new PageScoreDaoImpl();
		imp.setPageStatus(pageId, status);

		setRetMsg(msg, 200, "OK");
		return jsonView(msg);
	}

	@RequestMapping("/admin/control/detectedHostV2")
	public Object detectedHost(
			@RequestParam(required = true, defaultValue = "") String host,
			@RequestParam(required = false) String status_p,
			@CookieValue(value = "u", defaultValue = "") String cookieId)
			throws Exception {
		Map<String, Object> msg = checkLogin(cookieId);
		if (!isLogin(msg)) {
			return msg.get(this.loginRedirectKey);
		}
		int status = this.parseInt(status_p, PageDetectedVo.STATUS_ALL);
		PageScoreDao impl = new PageScoreDaoImpl();
		List<PageDetectedVo> vos = impl.selectHostDetectedPages(host, status);
		msg.put("host", host);
		msg.put("vos", vos);

		return new ModelAndView("metronics.detectedHostV2", "msg", msg);
	}

	@RequestMapping("/admin/control/detectedPageList")
	public Object detectedPageList(
			@RequestParam(required = false) String status_p,
			@RequestParam(required = false) String pageIndex_p,
			@CookieValue(value = "u", defaultValue = "") String cookieId)
			throws Exception {
		Map<String, Object> msg = checkLogin(cookieId);
		if (!isLogin(msg)) {
			return msg.get(this.loginRedirectKey);
		}

		int status = this.parseInt(status_p, PageDetectedVo.STATUS_ALL);
		int pageIndex = this.parseInt(pageIndex_p, 1);

		PageScoreDao impl = new PageScoreDaoImpl();

		int totalItems = impl.selectPageDetectedNum(status);
		PageSplit pageSplit = new PageSplit(totalItems, pageSize, pageIndex, 5);

		List<PageDetectedVo> vos = impl.selectDetectedPages(status,
				pageSplit.getItemStartIndex(), pageSize);

		msg.put("total", totalItems);
		msg.put("pageSplit", pageSplit);
		msg.put("vos", vos);
		msg.put("statusName", PageDetectedVo.satusName(status));
		msg.put("status", status);

		return new ModelAndView("metronics.detectedPageList", "msg", msg);
	}

	@RequestMapping("/admin/control/getDetectedPageV2")
	public Object getDetectedPageV2(
			@RequestParam(required = false, defaultValue = "0") int pageId,
			@CookieValue(value = "u", defaultValue = "") String cookieId) {
		Map<String, Object> msg = checkLogin(cookieId);
		if (!isLogin(msg)) {
			return msg.get(this.loginRedirectKey);
		}

		PageScoreDao imp = new PageScoreDaoImpl();
		PageDetectedVo vo = null;
		if (pageId > 0) {
			vo = imp.selectDetectedPage(pageId);
		}
		if (vo == null) {
			setRetMsg(msg, 404, "没有找到页面， pageId=" + pageId);
		} else {
			setRetMsg(msg, 200, "OK");
			try {
				if (mockMdirPath != null) {
					log.warn("mock path %s", mockMdirPath);
					vo.setPath(mockMdirPath);
				}
				vo.loadSynDataFromMDir();
			} catch (Exception e) {
				e.printStackTrace();
				log.warn(e, "load score data fail");
			}
			msg.put("PageDetectedVo", vo);
		}
		return new ModelAndView("jsonView", "msg", msg);
	}

	@RequestMapping("/admin/control/login")
	public Object loginForm() {
		return new ModelAndView("metronics.loginHtml", "msg", this.newMsgMap());
	}

	@RequestMapping("/admin/control/loginPost")
	public Object loginPost(
			@RequestParam(required = false, defaultValue = "") String userId_p,
			@RequestParam(required = false, defaultValue = "") String password_p,
			HttpServletResponse response) {
		log.warn("try login! using userId:%s, password:%s", userId_p,
				password_p);
		UserLoginVo login = new UserLoginVo();
		login.setPassword(password_p);
		login.setUserId(userId_p);
		login.setIdInt(0);

		AdminUserVo adminUser = login.toAdminUserVo();
		if (isLogin(adminUser)) {
			String ckStr = adminUser.toCookieValue();
			Cookie cookie = new Cookie("u", ckStr);
			log.warn("ok login! using userId:%s, set cookie %s", userId_p,
					ckStr);
			cookie.setMaxAge(31 * 24 * 60 * 60);
			response.addCookie(cookie);
			return new RedirectView("detectedPageList.html");
		} else {
			log.warn("fail login! using userId:%s, password:%s", userId_p,
					password_p);
			return new RedirectView("login.html");
		}
	}

	private boolean isLogin(AdminUserVo adminUser) {
		if (adminUser != null) {
			if (adminUser.getStatus() >= 0) {
				return true;
			}
		}
		return false;
	}

	private boolean isLogin(Map<String, Object> msg) {
		AdminUserVo adminUser = (AdminUserVo) msg.get(this.loginKey);

		return isLogin(adminUser);
	}

	private Map<String, Object> checkLogin(String cookieId) {
		Map<String, Object> msg = this.newMsgMap();
		UserLoginVo vo = UserLoginVo.parseCookie(cookieId);
		AdminUserVo adminUser = vo.toAdminUserVo();
		String scoreStr = "";
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectMapper mapper = new ObjectMapper();
			JsonGenerator gen = mapper.getFactory().createGenerator(bos);

			gen.writeObject(MultiScorer.getInstance().getStringScore());
			scoreStr = new String(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		msg.put("scoreStr", scoreStr);
		msg.put(loginKey, adminUser);
		msg.put(loginRedirectKey, new RedirectView("login.html"));
		return msg;
	}

	@RequestMapping("/admin/control/detectedListV2")
	public Object detectedHostsList(
			@RequestParam(required = false, defaultValue = "1") int pageIndex,
			@RequestParam(defaultValue = "1") int sortBy,
			@RequestParam(required = false) String statusPara,
			@CookieValue(value = "u", defaultValue = "") String cookieId) {
		Map<String, Object> msg = checkLogin(cookieId);
		if (!isLogin(msg)) {
			return msg.get(this.loginRedirectKey);
		}

		int status = this.parseInt(statusPara, PageDetectedVo.STATUS_ALL);
		if (pageIndex < 1) {
			pageIndex = 1;
		}
		int offset = (pageIndex - 1) * pageSize;

		PageScoreDao imp = new PageScoreDaoImpl();
		List<HostDetectedVo> result = imp.selectHostDetectedList(offset,
				pageSize, status);

		int totalUnAudit = imp.selectHostDetectedHost(status);
		PageSplit pageSplit = new PageSplit(totalUnAudit, pageSize, pageIndex);
		msg.put("list", result);
		msg.put("total", totalUnAudit);
		msg.put("pageSplit", pageSplit);

		return new ModelAndView("metronics.detectedListV2", "msg", msg);
	}
}
