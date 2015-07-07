package textmatchers.pattern.def.reg;

public class RKB extends REnum {
	/**
	 * include lineSeperater
	 */
	final boolean includeSeperator;

	public RKB(int min, int max) {
		this(min, max, true);
	}

	public RKB(int count) {
		this(count, count, true);
	}

	public RKB() {
		this(1, 1, true);
	}

	public RKB(int min, int max, boolean includeSeperator) {
		super(min, max);
		this.includeSeperator = includeSeperator;
	}

	public RKB(int count, boolean includeSeperator) {
		super(count, count);
		this.includeSeperator = includeSeperator;
	}

	public RKB(boolean includeSeperator) {
		super(1, 1);
		this.includeSeperator = includeSeperator;
	}

	@Override
	public boolean accept(char c) {
		if (this.includeSeperator) {
			return Character.isWhitespace(c);
		} else {
			if (Character.isWhitespace(c)) {
				if (c == '\r' || c == '\n') {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public String defineInfo() {
		return "$KB";
	}

	public static void main(String[] a) {
		RLetter c = new RLetter();
		System.out.println(c.toString());
		System.out.println(c.defineInfo());
		System.out.println(Character.isWhitespace(' '));
		System.out.println(Character.isWhitespace('	'));
		System.out.println(Character.isWhitespace('\r'));
		System.out.println(Character.isWhitespace('\n'));

	}
}
