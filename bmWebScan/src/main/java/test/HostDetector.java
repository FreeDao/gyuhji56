package test;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.bmtech.utils.rds.RDS;

public class HostDetector {
	AtomicInteger error = new AtomicInteger(0);
	AtomicInteger ok = new AtomicInteger(0);
	LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	class HostFinder extends Thread {

		@Override
		public void run() {
			while (true) {
				String t = null;
				try {
					t = queue.take();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (t == null) {
					break;
				}

				try {
					InetAddress its = java.net.InetAddress.getByName(t);

					System.out.println(its + " OK " + ok.addAndGet(1)
							+ ", now " + t);
				} catch (Exception e) {

					try {
						setError.setString(1, t);
						setError.execute();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					System.out.println("ERROR " + error.addAndGet(1) + ", now "
							+ t);
					e.printStackTrace();
				}
			}
		}
	}

	RDS rds, setError;

	public void startDetect() throws SQLException {
		rds = RDS
				.getRDSByDefine("webscan",
						"select host from webscan_host_info where status = 1 order by rand()");
		setError = RDS.getRDSByDefine("webscan",
				"update webscan_host_info set status = 0 where host=?");
		ResultSet rs = rds.load();
		while (rs.next()) {
			queue.add(rs.getString(1));
		}
		System.out.println("load ok");
		int threads = 100;
		for (int x = 0; x < threads; x++) {
			HostFinder v = new HostFinder();
			v.start();
		}
	}

	public static void main(String[] args) throws SQLException {
		HostDetector d = new HostDetector();

		d.startDetect();
	}
}
