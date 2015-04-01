package com.bmtech.utils.systemWatcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.bmtech.utils.log.BmtLogger;
import com.bmtech.utils.security.BmAes;
import com.bmtech.utils.tcp.TCPClient;

public class RemoteWatchClient {
	TCPClient clt;
	private String encKey;

	public RemoteWatchClient(String address, int port, String enc)
			throws IOException {
		int timeout = 10000;
		String timeoutStr = System.getProperty("watcherTimeout");
		if (timeoutStr != null) {
			timeout = Integer.parseInt(timeoutStr);
		} else {
			System.out.println("timeout set to " + timeout);
		}
		clt = new TCPClient(address, port, timeout);
		this.encKey = enc;
	}

	public RemoteWatchClient(int port, String enc) throws IOException {
		this("127.0.0.1", port, enc);
	}

	public void close() {
		if (clt != null) {
			clt.close();
		}
	}

	public String writeCommand(String cmd) throws IOException {

		byte[] bCmd = BmAes.encrypt(encKey, cmd);
		clt.write(bCmd);
		ByteArrayOutputStream bops = new ByteArrayOutputStream();
		long readed = clt.read(bops);
		if (readed < 1) {
			return "*** nothing read ***";
		}
		return BmAes.decrypt(encKey, bops.toByteArray(), 0, (int) readed);
	}

	public void stop() throws IOException {
		BmtLogger.log.warn("try stop %s:%s", clt.getSa().getAddress()
				.getHostAddress(), clt.getSa().getPort());
		String ret = writeCommand(WatcherAction.STOP);
		BmtLogger.log.warn("from %s:%s, got reply '%s'", clt.getSa()
				.getAddress().getHostAddress(), clt.getSa().getPort(), ret);
	}

	@Override
	public void finalize() {
		close();
	}

}
