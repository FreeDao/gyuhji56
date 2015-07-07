package textmatchers.pattern.def.reg;

public class RHZ extends RCharInner {

	public RHZ(int min, int max) {
		super(min, max);
	}

	public RHZ(int count) {
		super(count, count);
	}

	public RHZ() {
		super(1, 1);
	}

	@Override
	public boolean accept(char c) {
		return c > 255 && Character.isLetter((int) c);
	}

	@Override
	public String defineInfo() {
		return "$HZ";
	}
}
