package textmatchers.pattern.def.reg;

/**
 * any char except \r \n
 * 
 * @author mudie
 * 
 */
public class RRY extends RCharInner {
	public RRY() {
		super(1);
	}

	public RRY(int count) {
		super(count);
	}

	public RRY(int min, int max) {
		super(min, max);
	}

	@Override
	public boolean accept(char c) {
		if (c == '\r' || c == '\n')
			return false;
		return true;
	}

	@Override
	public String defineInfo() {
		return "*";
	}
}
