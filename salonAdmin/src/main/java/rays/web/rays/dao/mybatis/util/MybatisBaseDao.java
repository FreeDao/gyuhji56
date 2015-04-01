package rays.web.rays.dao.mybatis.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class MybatisBaseDao {

	private static SqlSessionFactory sessionFactory = null;
	static {
		sessionFactory = MyBatisHelper.getSessionFactory();
	}

	public SqlSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public SqlSession newSession() {
		return getSessionFactory().openSession(ExecutorType.BATCH);
	}

}
