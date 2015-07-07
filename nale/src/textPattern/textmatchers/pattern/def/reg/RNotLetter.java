package textmatchers.pattern.def.reg;

public class RNotLetter extends RCharInner {
	/**
	 * 
	 * @param min
	 * @param max
	 */
	public RNotLetter(int min, int max) {
		super(min, max);
	}

	public RNotLetter(int count) {
		super(count);
	}

	public RNotLetter() {
		super(1);
	}

	@Override
	public boolean accept(char c) {
		return !Character.isLetter(c);
	}

	@Override
	public String defineInfo() {
		return "{!rn}";
	}

	public static void main(String[] a) {
		RNotLetter c = new RNotLetter();
		System.out.println(c.toString());
		System.out.println(c.defineInfo());
	}
}
