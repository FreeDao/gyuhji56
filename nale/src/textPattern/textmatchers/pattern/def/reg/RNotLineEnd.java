package textmatchers.pattern.def.reg;

/**
 * any char except \r \n
 * 
 * @author mudie
 * 
 */
public class RNotLineEnd extends RCharInner {
	public RNotLineEnd() {
		super(1);
	}

	public RNotLineEnd(int count) {
		super(count);
	}

	public RNotLineEnd(int min, int max) {
		super(min, max);
	}

	@Override
	public boolean accept(char c) {
		if (c == '\r' || c == '\n')
			return false;
		return true;
	}

}
