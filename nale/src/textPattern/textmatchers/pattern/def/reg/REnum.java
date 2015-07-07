package textmatchers.pattern.def.reg;

public class REnum extends RCharInner {

	final char chars[];

	public REnum(int min, int max, char... chars) {
		super(min, max);
		this.chars = chars;
	}

	public REnum(int count, char... chars) {
		this(count, count, chars);
	}

	public REnum(char... chars) {
		this(1, 1, chars);
	}

	@Override
	public boolean accept(char c) {
		for (char ce : chars) {
			if (ce == c)
				return true;
		}
		return false;
	}

	@Override
	public String defineInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("chars(");
		for (char c : chars) {

			sb.append(c);
		}
		sb.append(")");
		return sb.toString();
	}
}
