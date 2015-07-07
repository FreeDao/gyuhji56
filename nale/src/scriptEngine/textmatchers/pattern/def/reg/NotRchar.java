package textmatchers.pattern.def.reg;

public class NotRchar extends RCharInner {
	public final RCharInner rChar;

	public NotRchar(int min, int max, RCharInner rChar) {
		super(min, max);
		this.rChar = rChar;
	}

	public NotRchar(int count, RCharInner rChar) {
		this(count, count, rChar);
	}

	public NotRchar(RCharInner rChar) {
		this(1, 1, rChar);
	}

	@Override
	public boolean accept(char c) {
		return !rChar.accept(c);
	}

	@Override
	public String defineInfo() {
		return "$!" + rChar;
	}
}
