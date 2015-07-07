package textmatchers.pattern.def.reg;

/**
 * any char except \r \n
 * 
 * @author mudie
 * 
 */
public class RAny extends RCharInner {
	public RAny() {
		super(1);
	}

	public RAny(int count) {
		super(count);
	}

	public RAny(int min, int max) {
		super(min, max);
	}

	@Override
	public boolean accept(char c) {
		return true;
	}

}
