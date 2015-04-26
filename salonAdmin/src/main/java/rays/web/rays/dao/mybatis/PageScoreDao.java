package rays.web.rays.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import rays.web.rays.vo.HostDetectedVo;
import rays.web.rays.vo.PageDetectedVo;

public interface PageScoreDao {

	public List<HostDetectedVo> selectHostDetectedList(
			@Param("offset") int offset, @Param("len") int len,
			@Param("status") int status);

	public List<PageDetectedVo> selectHostDetectedPages(
			@Param("host") String host, @Param("status") int status);

	public int selectHostDetectedHost(@Param("status") int status);

	public PageDetectedVo selectDetectedPage(@Param("pageId") int pageId);

	public void setPageStatus(@Param("pageId") int pageId,
			@Param("status") int status);

}
