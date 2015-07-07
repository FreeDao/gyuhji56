package textmatchers.pattern.def.reg;

/**
 * any char except \r \n
 * 
 * @author mudie
 * 
 */
public class RInSentence extends RCharInner {
	public RInSentence() {
		super(1);
	}

	public RInSentence(int count) {
		super(count);
	}

	public RInSentence(int min, int max) {
		super(min, max);
	}

	@Override
	public boolean accept(char c) {
		if (c == '\r' || c == '\n' || c == '。' || c == '！' || c == '!'
				|| c == '？')
			return false;
		return true;
	}

	@Override
	public String defineInfo() {
		return "in_sent";
	}

}
