package rays.web.rays.vo;

import java.util.Date;

public class SourceDetectVo {
	int id;
	String host;
	int evel_1_count;
	int level_2_count;
	int level_3_count;
	int scanNum;

	public int getLevel_3_count() {
		return level_3_count;
	}

	public void setLevel_3_count(int level_3_count) {
		this.level_3_count = level_3_count;
	}

	public int getScanNum() {
		return scanNum;
	}

	public void setScanNum(int scanNum) {
		this.scanNum = scanNum;
	}

	String path;
	int audit_status;
	Date inputtime;
	Date updatetime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host.toLowerCase().trim();
	}

	public int getEvel_1_count() {
		return evel_1_count;
	}

	public void setEvel_1_count(int evel_1_count) {
		this.evel_1_count = evel_1_count;
	}

	public int getLevel_2_count() {
		return level_2_count;
	}

	public void setLevel_2_count(int level_2_count) {
		this.level_2_count = level_2_count;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getAudit_status() {
		return audit_status;
	}

	public void setAudit_status(int audit_status) {
		this.audit_status = audit_status;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Override
	public String toString() {
		return "SourceDetectVo [id=" + id + ", host=" + host
				+ ", evel_1_count=" + evel_1_count + ", level_2_count="
				+ level_2_count + ", level_3_count=" + level_3_count
				+ ", scanNum=" + scanNum + ", path=" + path + ", audit_status="
				+ audit_status + ", inputtime=" + inputtime + ", updatetime="
				+ updatetime + "]";
	}

}
