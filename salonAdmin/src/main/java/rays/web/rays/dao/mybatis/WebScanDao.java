package rays.web.rays.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import rays.web.rays.vo.SourceDetectVo;

public interface WebScanDao {
	public SourceDetectVo selectSourceDetectedById(int varRuleId)
			throws Exception;

	public List<SourceDetectVo> selectSourceDetectedList(
			@Param("offset") int offset, @Param("len") int len);

	int selectTotalUnAudit();

}
