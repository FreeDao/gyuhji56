package textmatchers.pattern.def.reg;

public class RLetter extends RCharInner {
	/**
	 * 
	 * @param min
	 * @param max
	 */
	public RLetter(int min, int max) {
		super(min, max);
	}

	public RLetter(int count) {
		super(count);
	}

	public RLetter() {
		super(1);
	}

	@Override
	public boolean accept(char c) {
		return Character.isLetter(c);
	}

	public static void main(String[] a) {
		RLetter c = new RLetter();
		System.out.println(c.toString());
		System.out.println(c.defineInfo());
	}
}
