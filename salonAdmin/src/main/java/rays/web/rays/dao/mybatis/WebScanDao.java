package rays.web.rays.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import rays.web.rays.vo.SourceDetectVo;

public interface WebScanDao {
	public SourceDetectVo selectSourceDetectedById(@Param("id") int id)
			throws Exception;

	public List<SourceDetectVo> selectSourceDetectedList(
			@Param("offset") int offset, @Param("len") int len,
			@Param("onlyUnAudit") boolean onlyUnAudit);

	int selectTotalUnAudit();

	public List<SourceDetectVo> selectSourceDetectedListByHostNames(
			@Param("hosts") List<String> hosts,
			@Param("onlyUnAudit") boolean onlyUnAudit);

	void setHostStatus(@Param("hostName") String hostName,
			@Param("statusCode") int statusCode);
}
