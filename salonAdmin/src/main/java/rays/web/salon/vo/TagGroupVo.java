package rays.web.salon.vo;

import java.util.Date;

import com.bmtech.utils.Misc;

public class TagGroupVo {

	private int groupId;
	private String groupName;
	private long updateTime;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime.getTime();
	}

	@Override
	public String toString() {
		return "TagGroupVo [groupId=" + groupId + ", groupName=" + groupName
				+ ", updateTime=" + Misc.timeStr(updateTime) + "]";
	}

}
