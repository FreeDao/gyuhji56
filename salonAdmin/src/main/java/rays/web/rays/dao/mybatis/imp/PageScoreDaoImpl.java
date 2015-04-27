package rays.web.rays.dao.mybatis.imp;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import rays.web.rays.dao.mybatis.PageScoreDao;
import rays.web.rays.dao.mybatis.util.MybatisBaseDao;
import rays.web.rays.vo.HostDetectedVo;
import rays.web.rays.vo.PageDetectedVo;

public class PageScoreDaoImpl extends MybatisBaseDao implements PageScoreDao {

	@Override
	public List<HostDetectedVo> selectHostDetectedList(int offset, int len,
			int status) {
		SqlSession session = this.newSession();
		try {
			PageScoreDao dao = session.getMapper(PageScoreDao.class);
			return dao.selectHostDetectedList(offset, len, status);
		} finally {
			session.close();
		}
	}

	@Override
	public List<PageDetectedVo> selectHostDetectedPages(String host, int status) {
		SqlSession session = this.newSession();
		try {
			PageScoreDao dao = session.getMapper(PageScoreDao.class);
			return dao.selectHostDetectedPages(host, status);
		} finally {
			session.close();
		}
	}

	@Override
	public int selectHostDetectedHost(int status) {
		SqlSession session = this.newSession();
		try {
			PageScoreDao dao = session.getMapper(PageScoreDao.class);
			return dao.selectHostDetectedHost(status);
		} finally {
			session.close();
		}
	}

	@Override
	public PageDetectedVo selectDetectedPage(int pageId) {
		SqlSession session = this.newSession();
		try {
			PageScoreDao dao = session.getMapper(PageScoreDao.class);
			return dao.selectDetectedPage(pageId);
		} finally {
			session.close();
		}
	}

	@Override
	public void setPageStatus(int pageId, int status) {
		SqlSession session = this.newSession();
		try {
			PageScoreDao dao = session.getMapper(PageScoreDao.class);
			dao.setPageStatus(pageId, status);
			session.commit();
		} finally {
			session.close();
		}
	}

	@Override
	public List<PageDetectedVo> selectDetectedPages(int status, int offset,
			int len) {
		SqlSession session = this.newSession();
		try {
			PageScoreDao dao = session.getMapper(PageScoreDao.class);
			return dao.selectDetectedPages(status, offset, len);
		} finally {
			session.close();
		}
	}

	@Override
	public int selectPageDetectedNum(int status) {
		SqlSession session = this.newSession();
		try {
			PageScoreDao dao = session.getMapper(PageScoreDao.class);
			return dao.selectPageDetectedNum(status);
		} finally {
			session.close();
		}
	}

}
