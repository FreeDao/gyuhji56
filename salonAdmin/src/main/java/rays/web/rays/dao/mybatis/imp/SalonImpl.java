package rays.web.rays.dao.mybatis.imp;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import rays.web.rays.dao.mybatis.Salon;
import rays.web.rays.dao.mybatis.util.MybatisBaseDao;
import rays.web.salon.vo.ArtTagVo;
import rays.web.salon.vo.TagGroupVo;
import rays.web.salon.vo.TagNameVo;

public class SalonImpl extends MybatisBaseDao implements Salon {

	@Override
	public TagNameVo getTagNameById(int tagNameId) {
		SqlSession session = this.newSession();
		try {
			Salon dao = session.getMapper(Salon.class);
			return dao.getTagNameById(tagNameId);
		} finally {
			session.close();
		}
	}

	@Override
	public TagGroupVo getTagGroupById(int groupId) {
		SqlSession session = this.newSession();
		try {
			Salon dao = session.getMapper(Salon.class);
			return dao.getTagGroupById(groupId);
		} finally {
			session.close();
		}
	}

	@Override
	public List<ArtTagVo> getArtTagById(int artId) {
		SqlSession session = this.newSession();
		try {
			Salon dao = session.getMapper(Salon.class);
			return dao.getArtTagById(artId);
		} finally {
			session.close();
		}
	}

	public static void main(String[] args) throws Exception {
		SalonImpl impl = new SalonImpl();
		System.out.println(impl.getArtTagById(1));
		System.out.println(impl.getTagGroupById(1));
		System.out.println(impl.getTagNameById(1));
	}

}
