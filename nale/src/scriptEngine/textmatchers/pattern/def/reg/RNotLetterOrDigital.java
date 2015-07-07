package textmatchers.pattern.def.reg;

public class RNotLetterOrDigital extends RCharInner {
	/**
	 * 
	 * @param min
	 * @param max
	 */
	public RNotLetterOrDigital(int min, int max) {
		super(min, max);
	}

	public RNotLetterOrDigital(int count) {
		super(count);
	}

	public RNotLetterOrDigital() {
		super(1);
	}

	@Override
	public boolean accept(char c) {
		return !Character.isLetterOrDigit(c);
	}

	@Override
	public String defineInfo() {
		return "$!NotLetter";
	}

	public static void main(String[] a) {
		RNotLetterOrDigital c = new RNotLetterOrDigital();
		System.out.println(c.toString());
		System.out.println(c.defineInfo());
	}
}
