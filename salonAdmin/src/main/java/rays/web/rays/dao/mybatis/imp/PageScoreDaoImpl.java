package rays.web.rays.dao.mybatis.imp;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import rays.web.rays.dao.mybatis.PageScoreDao;
import rays.web.rays.dao.mybatis.util.MybatisBaseDao;
import rays.web.rays.vo.HostDetectedVo;
import rays.web.rays.vo.PageDetectedVo;

public class PageScoreDaoImpl extends MybatisBaseDao implements PageScoreDao {

	@Override
	public List<HostDetectedVo> selectHostDetectedList(int offset, int len) {
		SqlSession session = this.newSession();
		try {
			PageScoreDao dao = session.getMapper(PageScoreDao.class);
			return dao.selectHostDetectedList(offset, len);
		} finally {
			session.close();
		}
	}

	@Override
	public List<PageDetectedVo> selectHostDetectedPages(String host) {
		SqlSession session = this.newSession();
		try {
			PageScoreDao dao = session.getMapper(PageScoreDao.class);
			return dao.selectHostDetectedPages(host);
		} finally {
			session.close();
		}
	}

}
