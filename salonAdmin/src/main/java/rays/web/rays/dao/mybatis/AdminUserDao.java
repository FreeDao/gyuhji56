package rays.web.rays.dao.mybatis;

import org.apache.ibatis.annotations.Param;

import rays.web.rays.vo.AdminUserVo;

public interface AdminUserDao {
	public AdminUserVo selectAdminUser(@Param("userId") String userId);
}
