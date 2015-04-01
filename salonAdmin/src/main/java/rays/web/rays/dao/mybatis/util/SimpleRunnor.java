package rays.web.rays.dao.mybatis.util;

import org.apache.ibatis.session.SqlSession;

public abstract class SimpleRunnor {
	protected SqlSession session;

	public void setSession(SqlSession session) {
		this.session = session;

	}

	public abstract Object run() throws Exception;

}
