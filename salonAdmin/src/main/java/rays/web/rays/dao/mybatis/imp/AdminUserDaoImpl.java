package rays.web.rays.dao.mybatis.imp;

import org.apache.ibatis.session.SqlSession;

import rays.web.rays.dao.mybatis.AdminUserDao;
import rays.web.rays.dao.mybatis.util.MybatisBaseDao;
import rays.web.rays.vo.AdminUserVo;

public class AdminUserDaoImpl extends MybatisBaseDao implements AdminUserDao {

	@Override
	public AdminUserVo selectAdminUser(String userId) {
		SqlSession session = this.newSession();
		try {
			AdminUserDao dao = session.getMapper(AdminUserDao.class);
			return dao.selectAdminUser(userId);
		} finally {
			session.close();
		}
	}
}
