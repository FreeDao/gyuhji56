package textmatchers.pattern.def;

public class MString {
	private final String str;
	public final int lineOffset;
	private final int fromIndex;

	public MString(String str) {
		this(str, 0, 0);
	}

	public MString(String str, int lineOffset) {
		this(str, 0, lineOffset);
	}

	private MString(String str, int from, int lineOffset) {
		this.fromIndex = from;
		this.str = str;
		this.lineOffset = lineOffset;
	}

	public static MString newStr(String x) {
		return new MString(x, 0);
	}

	public static MString newStr(String x, int lineOffset) {
		return new MString(x, lineOffset);
	}

	public MString substring(Integer from) {
		return new MString(str, from + fromIndex, lineOffset + from);
	}

	public int length() {
		return str.length() - fromIndex;
	}

	public boolean startsWith(String str) {
		int len = str.length();
		for (int x = 0; x < len; x++) {
			if (str.charAt(x) != this.str.charAt(this.fromIndex + x)) {
				return false;
			}
		}
		return true;
	}

	public char charAt(int x) {
		return str.charAt(x + this.fromIndex);
	}

	public String getStr() {
		return str.substring(fromIndex);
	}

	@Override
	public String toString() {
		return this.getStr();
	}
}