package com.bmtech.util.nashorn;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.concurrent.locks.ReentrantLock;

import javax.script.ScriptEngine;

import com.bmtech.utils.Misc;
import com.bmtech.utils.io.LineWriter;

public class JjsEmulate {
	class HisWriter {
		LineWriter historyWriter;
		File file;

		public void setFile(File file) throws IOException {
			this.file = file;
			if (historyWriter != null) {
				this.historyWriter.close();
			}
			this.historyWriter = new LineWriter(file, true);
		}

		public void writeLine(String replace) throws IOException {
			historyWriter.writeLine(replace);

		}

		public void flush() throws IOException {
			historyWriter.flush();
		}
	}

	InputStream ips;
	OutputStream out;
	InputStream eps;
	Process proc;
	private ReentrantLock lock = new ReentrantLock();
	private HisWriter historyWriter = new HisWriter();

	public JjsEmulate(String cmd) throws IOException {
		proc = Runtime.getRuntime().exec(cmd);

		ips = proc.getInputStream();
		eps = proc.getErrorStream();
		out = proc.getOutputStream();
		newHistory();
	}

	private void newHistory() throws IOException {
		File base = new File("nashorn.history");
		base.mkdir();
		File file;
		File fs[] = base.listFiles();
		for (int x = 1;; x++) {
			String head = String.format("%04d", x) + ".";
			boolean found = false;
			for (File fx : fs) {
				if (fx.getName().startsWith(head)) {
					found = true;
					break;
				}
			}
			if (found) {
				continue;
			}
			String fName = head + Misc.timeStr(System.currentTimeMillis());
			file = new File(base, fName + ".js");
			break;
		}
		historyWriter.setFile(file);

	}

	private void startReadThreads() {
		new ReadConsole(ips, "", false).start();
		new ReadConsole(eps, "", true).start();
	}

	public void emulate() {
		startReadThreads();
		int lineNo = 0;
		while (true) {
			lineNo++;
			try {

				String input;
				if (lineNo == 1) {
					input = "load('config/nashorn/jjsEnv.js');";
					input = input
							+ "var hisFile = "
							+ this.jsString(this.historyWriter.file
									.getAbsoluteFile().getCanonicalPath());
				} else {
					input = readString();
				}
				String[] cmd = rewrite(input);
				history(cmd[1]);
				out.write(cmd[0].getBytes());
				out.write("\r\n".getBytes());
				out.flush();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void history(String input) throws IOException {
		historyWriter.writeLine(input.replace("\t \t", "\n"));
		historyWriter.flush();
	}

	private String[] rewrite(String input) {
		String[] ret = new String[] { input, input };
		if (input.trim().startsWith("//")) {
			try {
				ScriptEngine engine = NashornEngine.getEngine();
				engine.eval("load('config/nashorn/jjsinput.js');");
				input = engine.eval("jjsInput.input('" + input + "')")
						.toString().trim();
				ret[1] = input;// history log

				String ___internalFunc1 = String.format(
						"var ___internalFunc = %s", jsString(input));

				String ___internalFunc2 = ___internalFunc1 + "\n"
						+ "var ___internalFunc2 = eval(___internalFunc);";
				// System.out.println(___internalFunc2);
				ret[0] = ___internalFunc2;

				// System.out.println("ret is " + input);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
	}

	private String jsString(String str) throws IOException {
		return String.format("java.net.URLDecoder.decode('%s', 'utf-8')",
				URLEncoder.encode(str, "utf-8"));
	}

	class ReadConsole extends Thread {
		InputStream inputps;
		String prefix = "";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final boolean isErrorInput;

		ReadConsole(InputStream ips, String prefix, boolean isErrorInput) {
			this.inputps = ips;
			this.prefix = prefix;
			this.isErrorInput = isErrorInput;
		}

		@Override
		public void run() {
			while (true) {
				try {
					read();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void read() throws IOException, InterruptedException {

			while (true) {
				if (inputps.available() == 0) {
					Thread.sleep(10);
					if (inputps.available() == 0) {
						if (bos.size() > 0) {
							print();
						}
					}
				}
				int i = inputps.read();
				if (!lock.isHeldByCurrentThread()) {
					lock.lock();
				}
				// System.out.println(i);
				if (i == '\n') {
					bos.write(i);
					print();
				} else {
					bos.write(i);
				}
			}
		}

		void print() throws IOException, InterruptedException {
			byte[] bs = bos.toByteArray();
			// for (byte c : bs) {
			// System.out.println(c);
			// }
			String str = new String(bs);
			// boolean shouldNotify = false;
			if (str.equals("jjs> ")) {
				lock.unlock();
				Thread.sleep(100);
				lock.lock();
				// if (this.isErrorInput) {
				// shouldNotify = true;
				// }
			}
			System.out.print(prefix + str);
			lock.unlock();
			bos.reset();
			// if (shouldNotify) {
			// synchronized (wn) {
			// wn.notifyAll();
			// }
			// }
		}
	}

	private void waitFor() throws Exception {
		proc.waitFor();
		System.exit(0);
	}

	public static String readString() {
		while (true) {
			byte[] bs = new byte[4096];
			try {
				System.in.read(bs);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new String(bs).trim();
		}
	}

	public static void main(String[] args) throws Exception {
		JjsEmulate emu = new JjsEmulate(args[0]);
		emu.emulate();
		emu.waitFor();
	}
}
