package rays.web.rays.vo;

import java.util.Date;

public class VarRuleVO {
	private int id;
	private String varName;
	private String varScript;
	private String semName;
	private String semNameSuffix;
	private Date createTime;
	private Date updateTime;
	private boolean isDelete;

	@Override
	public String toString() {
		return "VarRuleVO [id=" + id + ", varName=" + varName + ", varScript="
				+ varScript + ", semName=" + semName + ", semNameSuffix="
				+ semNameSuffix + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", isDelete=" + isDelete + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getVarScript() {
		return varScript;
	}

	public void setVarScript(String varScript) {
		this.varScript = varScript;
	}

	public String getSemName() {
		return semName;
	}

	public void setSemName(String semName) {
		this.semName = semName;
	}

	public String getSemNameSuffix() {
		return semNameSuffix;
	}

	public void setSemNameSuffix(String semNameSuffix) {
		this.semNameSuffix = semNameSuffix;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
}
