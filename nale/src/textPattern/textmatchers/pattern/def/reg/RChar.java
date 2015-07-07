package textmatchers.pattern.def.reg;

public class RChar extends RCharInner {
	final char c;

	public RChar(char c) {
		super(1);
		this.c = c;
	}

	@Override
	public boolean accept(char c) {
		return this.c == c;
	}

	public static void main(String[] a) {
		RChar c = new RChar('你');
		System.out.println(c.toString());
		System.out.println(c.defineInfo());
	}

	@Override
	public String defineInfo() {
		return c + "";
	}
}
