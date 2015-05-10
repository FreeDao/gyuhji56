package rays.web.rays.vo;

import java.io.File;
import java.util.Date;

import rays.web.salon.conf.ScoredDataDir;

import com.bmtech.spider.core.util.SiteMDirReader;
import com.bmtech.spider.core.util.SynCombin.DecodeSynCombin;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.log.LogHelper;

public class PageDetectedVo {
	LogHelper log = new LogHelper("pageDetectedVo");
	public static int STATUS_ALL = -1;
	public static int STATUS_UNAUDIT = 0;
	public static int STATUS_NOT = 3;
	public static int STATUS_DISCARD = 6;
	public static int STATUS_OK = 9;
	public static int STATUS_MIDIFIED = 9999;

	public static Object satusName(int status) {
		if (status < 0) {
			return "全部";
		} else if (status == 0) {
			return "待处理";
		} else if (status == 9999) {
			return "已处理";
		} else if (status == 3) {
			return "已忽略";
		} else if (status == 9) {
			return "已接受";
		} else if (status == 6) {
			return "被放弃";
		} else {
			return "未知:" + status;
		}
	}

	private int id;
	private String host;
	private int score;
	private int audit_status;
	private String checkBy;
	private Date update_time;
	private String title;
	private String url;
	private String path;
	private String content;

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

	public void loadSynDataFromMDir() throws Exception {
		log.info("getPageData path:%s", this.path);
		String fileName[] = MFile.parseUri(this.path);
		File mdirFile = ScoredDataDir.getScoredFile(fileName[0]);
		log.info("try open MDir %s", mdirFile);
		if (mdirFile != null) {
			MDir mdir = MDir.open(mdirFile);
			try {
				SiteMDirReader reader = new SiteMDirReader(mdir);
				DecodeSynCombin syn = reader.getFile(fileName[1]);
				this.content = syn.getHtml();
				if (this.content != null) {
					// Parser p = new Parser(this.content);
					// NodeList nl = p.parse(null);
					// this.content = nl.asString();
					// System.out.println(this.content);
					// this.content =
					// HtmlRemover.htmlToLineFormat(this.content).lines;
					// System.out.println(this.content);
					String[] lines = this.content.split("\n");
					StringBuilder sb = new StringBuilder();
					for (String line : lines) {
						line = line.trim();
						if (line.length() == 0) {
							continue;
						} else {
							sb.append(line);
							sb.append("<br>");
						}
					}
					this.content = sb.toString();
				}
			} finally {
				mdir.close();
			}
		}

	}

	@Override
	public String toString() {
		return "PageDetectedVo [id=" + id + ", host=" + host + ", score="
				+ score + ", audit_status=" + audit_status + ", checkBy="
				+ checkBy + ", update_time=" + update_time + ", title=" + title
				+ ", url=" + url + ", path=" + path + ", content="
				+ (content == null ? "null" : ".length" + content.length())
				+ "]";
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static void main(String[] args) {
		String xx = "a\n b\n \n \n\n \n \n\n \n";
		String[] lines = xx.split("\n");
		StringBuilder sb = new StringBuilder();
		for (String x : lines) {
			x = x.trim();
			if (x.length() == 0) {
				continue;
			} else {
				sb.append(x);
				sb.append("<br>");
			}
		}
		System.out.println(sb);
	}
}
