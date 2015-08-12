package rays.httpproxy;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.io.ConfigReader;

public final class ProxyConfig {

	private static final ProxyConfig instance = new ProxyConfig();
	private int readTimeout;
	private int connectTimeout;
	private File tmpDir;

	private ProxyConfig() {
		try {
			ConfigReader cr = new ConfigReader("config/proxy.properties",
					"main");

			String var = cr.getValue("tmpDir", "proxy-tmp");
			this.setTmpDir(new File(var));
			getTmpDir().mkdirs();

			this.setConnectTimeout(cr.getInt("connectTimeout", 3000));
			this.setReadTimeout(cr.getInt("readTimeout", 10000));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public File getTmpDir() {
		return tmpDir;
	}

	public File newTmpFile() {
		try {
			return File.createTempFile("httproxy", "pro", getTmpDir());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public static ProxyConfig getInstance() {
		return instance;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setTmpDir(File tmpDir) {
		this.tmpDir = tmpDir;
	}

}
