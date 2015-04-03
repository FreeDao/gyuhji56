package rays.web.rays.vo;

import java.io.File;
import java.util.Date;

public class SourceDetectVo {
	public static final int isAuditing = 0;
	public static final int isWatching = 9;
	public static final int isIgnored = 2;
	int id;
	String host;
	int level_1_count;
	int level_2_count;
	int level_3_count;
	int scan_num;

	public int getLevel_3_count() {
		return level_3_count;
	}

	public void setLevel_3_count(int level_3_count) {
		this.level_3_count = level_3_count;
	}

	public int getScan_num() {
		return scan_num;
	}

	public void setScan_num(int scanNum) {
		this.scan_num = scanNum;
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

	public int getLevel_1_count() {
		return level_1_count;
	}

	public void setLevel_1_count(int evel_1_count) {
		this.level_1_count = evel_1_count;
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
				+ ", level_1_count=" + level_1_count + ", level_2_count="
				+ level_2_count + ", level_3_count=" + level_3_count
				+ ", scanNum=" + scan_num + ", path=" + path
				+ ", audit_status=" + audit_status + ", inputtime=" + inputtime
				+ ", updatetime=" + updatetime + "]";
	}

	public File toFile() {
		return new File(this.getPath());
	}

	public boolean isWatching() {
		return this.getAudit_status() == isWatching;
	}

	public boolean isAuditing() {
		return this.getAudit_status() == isAuditing;
	}

}
