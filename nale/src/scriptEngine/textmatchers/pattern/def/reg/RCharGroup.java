package textmatchers.pattern.def.reg;

public abstract class RCharGroup extends RCharInner {

	final char[][] p;

	public RCharGroup(int min, int max) {
		super(min, max);
		p = rewrite(makePattern());
	}

	public RCharGroup(int count) {
		this(count, count);
	}

	public RCharGroup() {
		this(1);
	}

	private char[][] rewrite(char[][] makePattern) {
		char[][] ret = new char[makePattern.length][];
		for (int x = 0; x < makePattern.length; x++) {
			char[] now = makePattern[x];
			if (now.length == 1) {
				ret[x] = new char[] { now[0], now[0] };
			} else if (now.length == 2) {
				if (now[0] > now[1]) {
					throw new RuntimeException(
							"error for RCharGroup's group, maybe need replace for "
									+ now[0] + "-" + now[1]);
				}
				ret[x] = new char[] { now[0], now[1] };
			} else {
				new RuntimeException(
						"error for RCharGroup's group, elements count error:"
								+ now.length);
			}
		}
		return ret;
	}

	protected abstract char[][] makePattern();

	@Override
	public boolean accept(char c) {
		for (char[] grp : p) {
			if (c >= grp[0] && c <= grp[1]) {
				return true;
			}
		}
		return false;
	}

	public String getDefine() {
		StringBuilder sb = new StringBuilder();
		for (char[] grp : p) {
			sb.append('[');
			sb.append(grp[0]);
			sb.append(grp[1]);
			sb.append(']');
		}
		return sb.toString();
	}

}
