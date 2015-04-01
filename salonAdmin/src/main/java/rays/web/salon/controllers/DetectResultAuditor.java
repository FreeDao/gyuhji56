package rays.web.salon.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import rays.web.rays.dao.mybatis.imp.WebScanDaoImpl;
import rays.web.rays.vo.SourceDetectVo;

import com.bmtech.utils.PageSplit;

@Controller
public class DetectResultAuditor<pageSplit> {
	final int pageSize = 20;

	@RequestMapping("/listDetected")
	public ModelAndView listDetected(
			@RequestParam(required = false, defaultValue = "1") int pageIndex,
			@RequestParam(defaultValue = "1") int sortBy) {
		if (pageIndex < 1) {
			pageIndex = 1;

		}
		int offset = (pageIndex - 1) * pageSize;

		WebScanDaoImpl imp = new WebScanDaoImpl();
		List<SourceDetectVo> result = imp.selectSourceDetectedList(offset,
				pageSize);
		int totalUnAudit = imp.selectTotalUnAudit();
		HashMap<String, Object> map = new HashMap<String, Object>();
		PageSplit pageSplit = new PageSplit(totalUnAudit, pageSize, pageIndex);
		map.put("list", result);
		map.put("total", totalUnAudit);
		map.put("pageSplit", pageSplit);
		return new ModelAndView("detectedHost", "msg", map);
	}
}
