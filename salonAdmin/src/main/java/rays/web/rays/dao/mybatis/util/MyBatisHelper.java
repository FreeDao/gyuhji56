package rays.web.rays.dao.mybatis.util;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * ClassName:AuthBaseService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-8-25 上午10:55:27 <br/>
 * 
 * @author zhenglq
 * @version
 * @since JDK 1.7
 * @see
 */
public class MyBatisHelper {

	private static MyBatisHelper myBatisHelper = null;
	private SqlSessionFactory sessionFactory = null;
	private final String resource = "mybatis-config.xml";

	private MyBatisHelper() {
		Reader readReader;
		try {
			readReader = Resources.getResourceAsReader(resource);
			// Class c = Class.forName("rays.web.rays.vo.VarRuleVO");
			// System.out.println(c);
			sessionFactory = new SqlSessionFactoryBuilder().build(readReader,
					"dataSource");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 获取SqlSessionFactory<br/>
	 * 
	 * @return
	 * @author zhenglq
	 * @since JDK 1.7
	 */
	public static SqlSessionFactory getSessionFactory() {
		if (myBatisHelper == null) {
			myBatisHelper = new MyBatisHelper();
		}
		return myBatisHelper.sessionFactory;
	}

}
