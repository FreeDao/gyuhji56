package rays.web.salon.vo;

import java.util.Date;

import com.bmtech.utils.Misc;

public class ArtTagVo {

	private int relationId;
	private int tagId;
	private int groupId;
	private String tagName;
	private String groupName;
	private long updateTime;

	public int getRelationId() {
		return relationId;
	}

	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "ArtTagVo [relationId=" + relationId + ", tagId=" + tagId
				+ ", groupId=" + groupId + ", tagName=" + tagName
				+ ", groupName=" + groupName + ", updateTime="
				+ Misc.timeStr(updateTime) + "]";
	}

}
