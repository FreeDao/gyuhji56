package rays.web.rays.dao.mybatis.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
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
		return getSessionFactory().openSession(ExecutorType.SIMPLE);
	}

	public String escapeSql(String strOrg) {
		if (strOrg == null)
			return "";
		return StringEscapeUtils.escapeSql(strOrg.trim());
	}

	public List<String> escapeSql(List<String> lstOrg) {
		List<String> retList = new ArrayList<String>(lstOrg.size());
		for (int x = 0; x < lstOrg.size(); x++) {
			String str = lstOrg.get(x);
			str = escapeSql(str);
			retList.add(x, str);
		}
		return retList;

	}
}
