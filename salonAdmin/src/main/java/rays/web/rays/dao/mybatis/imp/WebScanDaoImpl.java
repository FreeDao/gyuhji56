package rays.web.rays.dao.mybatis.imp;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import rays.web.rays.dao.mybatis.WebScanDao;
import rays.web.rays.dao.mybatis.util.MybatisBaseDao;
import rays.web.rays.vo.SourceDetectVo;

public class WebScanDaoImpl extends MybatisBaseDao implements WebScanDao {

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
	public List<SourceDetectVo> selectSourceDetectedList(int offset, int len) {
		SqlSession session = this.newSession();
		try {
			WebScanDao dao = session.getMapper(WebScanDao.class);
			return dao.selectSourceDetectedList(offset, len);
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
}
