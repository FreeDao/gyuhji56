package rays.web.rays.dao.mybatis.imp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import rays.web.rays.dao.mybatis.WebScanDao;
import rays.web.rays.dao.mybatis.util.MybatisBaseDao;
import rays.web.rays.vo.DetectedPage;
import rays.web.rays.vo.SourceDetectVo;

import com.bmtech.utils.log.LogHelper;

public class WebScanDaoImpl extends MybatisBaseDao implements WebScanDao {

	private LogHelper log = new LogHelper(this.getClass().getName());

	@Override
	public SourceDetectVo selectSourceDetectedById(int varRuleId)
			throws Exception {
		SqlSession session = this.newSession();
		try {
			WebScanDao dao = session.getMapper(WebScanDao.class);
			return dao.selectSourceDetectedById(varRuleId);
		} finally {
			session.close();
		}
	}

	@Override
	public List<SourceDetectVo> selectSourceDetectedList(int offset, int len,
			boolean onlyUnAudit) {
		SqlSession session = this.newSession();
		try {
			WebScanDao dao = session.getMapper(WebScanDao.class);
			return dao.selectSourceDetectedList(offset, len, onlyUnAudit);
		} finally {
			session.close();
		}
	}

	@Override
	public int selectTotalUnAudit() {
		SqlSession session = this.newSession();
		try {
			WebScanDao dao = session.getMapper(WebScanDao.class);
			return dao.selectTotalUnAudit();
		} finally {
			session.close();
		}
	}

	@Override
	public List<SourceDetectVo> selectSourceDetectedListByHostNames(
			List<String> hosts, boolean onlyUnAudit) {
		hosts = escapeSql(hosts);
		SqlSession session = this.newSession();
		try {
			WebScanDao dao = session.getMapper(WebScanDao.class);
			return dao.selectSourceDetectedListByHostNames(hosts, onlyUnAudit);
		} finally {
			session.close();
		}
	}

	public Map<String, List<SourceDetectVo>> getDetectedHostMap(
			List<String> host) {
		List<SourceDetectVo> lst = this.selectSourceDetectedListByHostNames(
				host, false);
		HashMap<String, List<SourceDetectVo>> retMap = new HashMap<String, List<SourceDetectVo>>();
		for (SourceDetectVo vo : lst) {
			List<SourceDetectVo> hostList = retMap.get(vo.getHost());
			if (hostList == null) {
				hostList = new ArrayList<SourceDetectVo>();
				retMap.put(vo.getHost(), hostList);
			}
			hostList.add(vo);
		}
		return retMap;
	}

	public List<SourceDetectVo> getDetectedHost(String host) {
		List<String> hosts = new ArrayList<String>();
		hosts.add(host);

		return selectSourceDetectedListByHostNames(hosts, false);
	}

	public List<DetectedPage> getDetectedPages(String host) {
		return this.getDetectedPages(this.getDetectedHost(host));
	}

	public List<DetectedPage> getDetectedPages(List<SourceDetectVo> lst) {
		List<DetectedPage> pages = new ArrayList<DetectedPage>();
		for (SourceDetectVo vo : lst) {
			try {
				File toCheck = vo.toFile();
				log.info("checking %s", toCheck);
				if (toCheck.exists()) {
					File[] fs = toCheck.listFiles();
					for (File file : fs) {
						DetectedPage page = new DetectedPage(file);
						pages.add(page);
					}
				}

			} catch (Exception e) {
				log.error(e, "for input %s", vo);
			}
		}
		Collections.sort(pages, new Comparator<DetectedPage>() {

			@Override
			public int compare(DetectedPage o1, DetectedPage o2) {
				return o2.getScore() - o1.getScore();
			}

		});
		return pages;
	}

	@Override
	public void setHostStatus(String host, int statusCode) {
		log.info("set '%s' to %s", host, statusCode);
		host = escapeSql(host);
		log.info("set '%s' to %s", host, statusCode);
		SqlSession session = this.newSession();
		try {
			WebScanDao dao = session.getMapper(WebScanDao.class);
			dao.setHostStatus(host, statusCode);
			log.info("after set '%s' to %s", host, statusCode);
		} finally {
			session.close();
		}
	}
}
