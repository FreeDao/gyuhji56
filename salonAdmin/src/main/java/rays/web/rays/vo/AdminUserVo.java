package rays.web.rays.vo;

import java.io.ByteArrayOutputStream;

import com.bmtech.utils.bmfs.util.WriteProtocol;
import com.bmtech.utils.security.BmAes;
import com.bmtech.utils.security.Byte2Hex;

public class AdminUserVo {

	private int idInt;
	private String userId;
	private String password;
	private int status;
	private long createTime;
	private long updateTime;

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
		this.userId = userId.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password.trim();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "AdminUserVo [idInt=" + idInt + ", userId=" + userId
				+ ", password=" + password + ", status=" + status
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}

	public String toCookieValue() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			WriteProtocol prt = new WriteProtocol(bos);
			prt.writeI16((short) 3);
			prt.writeI32(this.idInt);
			prt.writeString(this.userId);
			prt.writeString(this.password);
			byte[] bs = bos.toByteArray();
			byte[] enc = BmAes.encrypt(UserLoginVo.encKey, bs);
			return Byte2Hex.byte2Hex(enc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
