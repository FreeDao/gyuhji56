package rays.web.rays.vo;

import java.util.Date;

public class PageDetectedVo {
	public static int STATUS_ALL = -1;
	public static int STATUS_UNAUDIT = 0;
	public static int STATUS_NOT = 3;
	public static int STATUS_OK = 9;
	private int id;
	private String host;
	private int score;
	private int audit_status;
	private String checkBy;
	private Date update_time;
	private String title;
	private String url;
	private String path;

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
		this.host = host;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getAudit_status() {
		return audit_status;
	}

	public void setAudit_status(int audit_status) {
		this.audit_status = audit_status;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCheckBy() {
		return checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

}
