package textmatchers.pattern.def.reg;

public class RBD extends RCharInner {
	/**
	 * 
	 * @param min
	 * @param max
	 */
	public RBD(int min, int max) {
		super(min, max);
	}

	public RBD(int count) {
		super(count);
	}

	public RBD() {
		super(1);
	}

	@Override
	public boolean accept(char c) {
		return !Character.isLetterOrDigit(c);
	}

	@Override
	public String defineInfo() {
		return "，。";
	}

	public static void main(String[] a) {
		RBD c = new RBD();
		System.out.println(c.toString());
		System.out.println(c.defineInfo());
	}
}
