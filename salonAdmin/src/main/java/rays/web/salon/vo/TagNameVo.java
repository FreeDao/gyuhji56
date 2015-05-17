package rays.web.salon.vo;

import java.util.Date;

import com.bmtech.utils.Misc;

public class TagNameVo {
	private int tagId;
	private int groupId;
	private String groupName;
	private String tagName;
	private long updateTime;

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
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

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public String toString() {
		return "TagNameVo [tagId=" + tagId + ", groupId=" + groupId
				+ ", groupName=" + groupName + ", tagName=" + tagName
				+ ", updateTime=" + Misc.timeStr(updateTime) + "]";
	}

}
