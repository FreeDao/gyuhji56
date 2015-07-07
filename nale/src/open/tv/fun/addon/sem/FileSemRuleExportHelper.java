package tv.fun.addon.sem;

import java.io.File;

import tv.fun.addon.SemToken;

import com.bmtech.utils.Charsets;
import com.bmtech.utils.Misc;
import com.bmtech.utils.io.FileGet;

public class FileSemRuleExportHelper extends SemRuleExportHelper {

	FileSemRuleExportHelper(String toExportVar, SemToken attachTo, String lines)
			throws Exception {
		super(toExportVar, attachTo, lines);
	}

	@Override
	protected String checkVarName(String toExportVar2) {
		return toExportVar2;
	}

	@Override
	public void save() throws Exception {
		throw new RuntimeException("not support save()");
	}

	public static FileSemRuleExportHelper getFromFile(File f) throws Exception {
		byte buf[] = FileGet.getBytes(f);
		String str = new String(buf, Charsets.UTF8_CS);
		String x = Misc.substring(str, "// export @", "\n");
		String name = f.getName();
		String semToken = Misc.substring(name, null, "--");
		return new FileSemRuleExportHelper(x, SemToken.toSemToken(semToken),
				str);
	}

}
