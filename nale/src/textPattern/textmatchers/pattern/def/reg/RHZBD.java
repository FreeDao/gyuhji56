package textmatchers.pattern.def.reg;

public class RHZBD extends RCharInner {

	public RHZBD(int min, int max) {
		super(min, max);
	}

	public RHZBD(int count) {
		super(count, count);
	}

	public RHZBD() {
		super(1, 1);
	}

	@Override
	public boolean accept(char c) {
		return c > 255 && !Character.isLetter(c);
	}

	@Override
	public String defineInfo() {
		return "$HZBD";
	}
}
