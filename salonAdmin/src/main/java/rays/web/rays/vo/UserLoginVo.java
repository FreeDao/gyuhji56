package rays.web.rays.vo;

import java.io.ByteArrayInputStream;

import rays.web.rays.dao.mybatis.imp.AdminUserDaoImpl;

import com.bmtech.utils.bmfs.util.ReadProtocol;
import com.bmtech.utils.security.BmAes;
import com.bmtech.utils.security.Byte2Hex;

public class UserLoginVo {
	public static final byte[] encKey = "tgy%^*9&oij".getBytes();
	private int idInt;
	private String userId;
	private String password;

	@Override
	public String toString() {
		return "UserLoginVo [idInt=" + idInt + ", userId=" + userId
				+ ", password=" + password + "]";
	}

	public static UserLoginVo parseCookie(String cookieId) {
		UserLoginVo vo = new UserLoginVo();
		byte[] cookieBytes = Byte2Hex.hex2Byte(cookieId);
		if (cookieId != null && cookieId.length() > 0) {
			try {
				byte[] bs = BmAes.decrypt(encKey, cookieBytes);

				ReadProtocol prt = new ReadProtocol(
						new ByteArrayInputStream(bs));
				int ver = prt.readI16();
				if (ver != 3) {
					throw new RuntimeException("expect ver 3, but get " + ver);
				}
				int idInt = prt.readI32();
				String id = prt.readString();
				String password = prt.readString();

				vo.setIdInt(idInt);
				vo.setUserId(id.trim());
				vo.setPassword(password.trim());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vo;
	}

	public AdminUserVo toAdminUserVo() {
		AdminUserDaoImpl impl = new AdminUserDaoImpl();
		AdminUserVo ret = impl.selectAdminUser(this.getUserId());
		if (!isLogin(ret, this)) {
			System.out.println("login fail! " + ret + " != " + this);
			return null;
		}
		return ret;

	}

	public static boolean isLogin(AdminUserVo adminUser, UserLoginVo vo) {
		if (vo == null || adminUser == null) {
			return false;
		}
		if (adminUser.getUserId().equals(vo.getUserId())) {
			if (adminUser.getPassword().equals(vo.getPassword())) {
				System.out.println("match, " + vo.getIdInt());
				if (vo.getIdInt() == 0 || adminUser.getIdInt() == vo.getIdInt()) {
					return true;
				}
			}
		}
		return false;
	}

	public int getIdInt() {
		return idInt;
	}

	public void setIdInt(int idInt) {
		this.idInt = idInt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
