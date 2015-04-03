package rays.web.salon.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractController {
	protected Map<String, Object> newMsgMap() {
		return new HashMap<String, Object>();
	}

	protected ModelAndView jsonView(Map<String, Object> msg) {
		return new ModelAndView("jsonView", "msg", msg);
	}

	protected void setRetMsg(Map<String, Object> msg, int retCode, String retMsg) {
		msg.put("retCode", retCode);
		msg.put("retMsg", retMsg);
	}

}
