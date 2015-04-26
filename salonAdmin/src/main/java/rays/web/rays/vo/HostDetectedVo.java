package rays.web.rays.vo;

public class HostDetectedVo {
	private String host;
	private int count;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "HostDetectedVo [host=" + host + ", count=" + count + "]";
	}

}
