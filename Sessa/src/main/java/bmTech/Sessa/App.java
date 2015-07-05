package bmTech.Sessa;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

	// public static final File sessaWorkDir;
	// static {
	// String sessaWorkDirStr = System.getProperty("sessa_work_dir");
	// if (sessaWorkDirStr == null) {
	// sessaWorkDirStr = "/Sessa";
	// }
	// try {
	// sessaWorkDir = Misc.besureDirExists(sessaWorkDirStr);
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	//
	// }

	public static void main(String[] args) throws IOException,
			InterruptedException {
		Console con = System.console();
		System.out.println("con " + con);
		new Thread() {

			@Override
			public void run() {

				while (true) {
					try {
						System.out.println(con.reader().markSupported());

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Scanner sc = new Scanner(con.reader());
					System.out.println("sc:" + sc);
					System.out.println("checking");
					String str = sc.findInLine("^//bi$");
					System.out.println(str);
					if (str != null) {
						System.out.println("sdfadsfa");
						sc.close();
						break;
					}
					sc.close();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}.start();

		while (true) {
			Thread.sleep(100);
		}
	}
}
