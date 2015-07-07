package textmatchers.pattern.def.reg;


public class RFW extends RCharInner {

	final char cStart, cEnd;

	public RFW(int min, int max, char cStart, char cEnd) {
		super(min, max);
		if (cStart > cEnd) {
			throw new RuntimeException(
					"can not instance a RRangeChar with start-end " + cStart
							+ "-" + cEnd);
		}
		this.cStart = cStart;
		this.cEnd = cEnd;
	}

	public RFW(int min, int max, char c) {
		this(min, max, c, c);
	}

	public RFW(int count, char c) {
		this(count, count, c, c);
	}

	public RFW(char c) {
		this(1, 1, c, c);
	}

	public RFW(int count, char cStart, char cEnd) {
		this(count, count, cStart, cEnd);
	}

	public RFW(char cStart, char cEnd) {
		this(1, 1, cStart, cEnd);
	}

	@Override
	public boolean accept(char c) {
		return (c >= this.cStart && c <= cEnd);
	}

	@Override
	public String defineInfo() {
		return "range(" + cStart + "," + this.cEnd + ")";
	}

}
