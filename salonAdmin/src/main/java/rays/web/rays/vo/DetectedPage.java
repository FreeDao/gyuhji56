package rays.web.rays.vo;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.io.FileGet;

public class DetectedPage {
	// 0072-I~~tmp~ws~sc-tmp~10737169.html.txt
	private int score;
	private final File file;
	String text;

	public DetectedPage(File file) throws Exception {
		if (!file.exists()) {
			throw new Exception("can not find DetectedPageInfo for name "
					+ file.getAbsolutePath());
		}
		this.file = file;
		String name = file.getName();
		String score = name.substring(0, 4);
		this.setScore(Integer.parseInt(score));
	}

	public String getText() throws IOException {
		if (text == null) {
			text = FileGet.getStr(file);
		}
		return text;
	}

	public String getWrapedText() throws IOException {
		String text = this.getText();
		text = text.replace("\r", "").replaceAll("[\t ]{1,}", " ")
				.replaceAll("\n \n", "\n").replaceAll("\n \n", "\n")
				.replace("\n", "<br>");
		return text;
	}

	public File getFile() {
		return file;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
