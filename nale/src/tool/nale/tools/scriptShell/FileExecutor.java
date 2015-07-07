package nale.tools.scriptShell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.bmtech.utils.io.FileGet;
import com.bmtech.utils.io.LineReader;

public class FileExecutor extends ScriptExecutor {
	public static final File workSpace = new File("./wkrspc");
	private final File script;
	private final File doc;
	private final File saveDir;

	public FileExecutor() throws IOException {
		super("草稿.nale");

		if (!workSpace.exists()) {
			workSpace.mkdirs();
		}
		script = new File(workSpace, super.scriptName);
		doc = new File(workSpace, "testDoc.txt");
		if (!script.exists()) {
			script.createNewFile();
		}
		if (!doc.exists()) {
			doc.createNewFile();
		}
		saveDir = new File(workSpace, "script.saved");
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
	}

	@Override
	protected String getInput() throws IOException {
		byte[] bs = FileGet.getBytes(doc);
		String ret = new String(bs, "utf-8");
		return ret;
	}

	@Override
	protected Iterator<String> getScriptIterator() throws IOException {
		LineReader lr = new LineReader(this.script, Charset.forName("utf-8"));
		return lr;
	}

	@Override
	public boolean save(String name) {
		File f = new File(this.saveDir, name + ".nale");
		if (f.exists()) {
			System.out.println("FAIL!不能保存文件为" + f.getName());
			return false;
		}
		if (this.script.length() == 0) {
			System.out.println("FAIL!不能保存空文件");
			return false;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			byte[] bs = FileGet.getBytes(this.script);
			fos.write(bs);
			fos.flush();
			this.script.delete();
			this.script.createNewFile();
			return true;
		} catch (FileNotFoundException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException exc) {
					// TODO Auto-generated catch block
					exc.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public boolean needSave() {
		return this.script.length() > 0;
	}

}
