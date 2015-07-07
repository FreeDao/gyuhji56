package nale.tools.cmdLine.cmds;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import nale.tools.cmdLine.DebugException;

import com.bmtech.utils.io.LineReader;

public class FileStringItrator implements Iterator<String> {
	public static String reminderPrefix = "//@No.";
	LineReader lr;
	private String lineReminder;

	FileStringItrator(File file) throws DebugException, IOException {
		if (!file.exists()) {
			throw new DebugException("file is not exists:" + file);
		}
		lr = new LineReader(file, "utf-8");
	}

	public String getLineReminder() {
		return this.lineReminder;
	}

	public File getFileFrom() {
		return lr.getFile();
	}

	@Override
	public void remove() {
		throw new DebugException("unsupport action #remove()");
	}

	private String current = null;
	private int lineNo = 0;

	@Override
	public boolean hasNext() {
		boolean found = findLine(true);
		if (found) {
			lineNo++;
		}
		return found;
	}

	private boolean findLine(boolean needReminder) {
		if (!lr.hasNext()) {
			return false;
		}
		String next = lr.next();
		if (next.startsWith(reminderPrefix)) {
			this.lineReminder = next;
			return findLine(false);
		} else {
			if (needReminder) {
				this.lineReminder = reminderPrefix + lineNo + " : " + next;
			}
			current = next;

			return true;
		}
	}

	@Override
	public String next() {
		return this.current;
	}
}
