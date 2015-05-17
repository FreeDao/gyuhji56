package rays.web.rays.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import rays.web.salon.vo.ArtTagVo;
import rays.web.salon.vo.TagGroupVo;
import rays.web.salon.vo.TagNameVo;

public interface Salon {

	public TagNameVo getTagNameById(@Param("tagNameId") int tagNameId);

	public TagGroupVo getTagGroupById(@Param("groupId") int groupId);

	public List<ArtTagVo> getArtTagById(@Param("artId") int artId);

}
