package com.bmtech.nale.addon.sem;

import com.bmtech.utils.ArgsParser;
import com.bmtech.utils.Consoler;

public class MatchResultLoop {
	String[] helps = new String[] {
			"mr 打印匹配日志。如果中途停顿，可使用q/quit停止程序。以下是支持的参数             ",
			"r	随机打印日志，为了避免总是打印开始的开始的记录，可采用此参数",
			"m	最多测试条目数， 如： m30打印30条。 默认值m20， 即测试20条 ",
			"f	指定从那一条开始， 如： f10，从第10条开始打印  ",
			"np	在没有匹配的时候不停顿，默认：在没有匹配的时候不停顿，继续向后扫描。          ",
			"nm	不显示匹配的结果，默认显示 。可通过mn命令禁止显示匹配结果",
			"nl	不显示内容行。配合mn使用，可以在存在匹配的情况下不现实任何内容，以突出未匹配内容" };

	public void loop() {
		while (true) {
			String cmd = Consoler.readString("> ").trim();
			try {
				if (cmd.equalsIgnoreCase("help")) {
					for (String x : helps) {
						System.out.println(x);
					}
				} else {
					process(cmd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void process(String cmd) {
		ArgsParser p = new ArgsParser(cmd);

		boolean rand = false;
		int from = 1;
		int max = 20;
		boolean pause = true;
		boolean showMatch = true;
		boolean showLine = true;
		System.out.println(p);
	}

	public static void main(String[] args) {
		MatchResultLoop ml = new MatchResultLoop();
		ml.loop();
	}
}
