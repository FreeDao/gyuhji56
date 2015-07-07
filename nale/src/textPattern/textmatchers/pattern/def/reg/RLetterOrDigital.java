package textmatchers.pattern.def.reg;

public class RLetterOrDigital extends RCharInner {
	/**
	 * 
	 * @param min
	 * @param max
	 */
	public RLetterOrDigital(int min, int max) {
		super(min, max);
	}

	public RLetterOrDigital(int count) {
		super(count);
	}

	public RLetterOrDigital() {
		super(1);
	}

	@Override
	public boolean accept(char c) {
		return Character.isLetterOrDigit(c);
	}

	public static void main(String[] a) {
		RLetterOrDigital c = new RLetterOrDigital();
		System.out.println(c.toString());
		System.out.println(c.defineInfo());
	}
}
