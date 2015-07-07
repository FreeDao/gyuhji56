package textmatchers.pattern.def.reg;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import textmatchers.pattern.def.MString;
import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.QPattern;
import textmatchers.pattern.def.QRCharResult;
import textmatchers.pattern.def.QResult;

public abstract class RCharInner implements QMatchable {
	private int min, max;

	public RCharInner(int min, int max) {
		if (min < 0 || min > max) {
			throw new RuntimeException("can not compile CRegChar{" + min + ","
					+ max + "}");
		}
		this.min = min;
		this.max = max;
	}

	public RCharInner(int count) {
		this(count, count);
	}

	public String defineInfo() {
		return "@" + this.getClass();
	}

	@Override
	public final String toString() {
		if (min == 1 && max == 1) {
			return defineInfo();
		} else {
			String num = ".";
			if (min == max) {
				num += min;
			} else {
				if (max == Integer.MAX_VALUE) {
					num += min + "-";
				} else {
					num += min + "-" + max;
				}
			}
			return defineInfo() + num;
		}

	}

	public abstract boolean accept(char c);

	@Override
	public final QResult match(MString str) {
		QRCharResult qer = new QRCharResult(this);
		int maxAccept = 0;
		if (this.min == 0) {
			qer.addMatch(0);
		}
		int len = str.length();
		for (int x = 0; x < len && x < max; x++) {
			char c = str.charAt(x);
			if (accept(c)) {
				maxAccept++;
				if (maxAccept >= this.min) {
					qer.addMatch(x + 1);
				}
			} else {
				break;
			}
		}

		return qer;
	}

	public String encode(char c) {
		try {
			return URLEncoder.encode(c + "", "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String encode(String str) {
		try {
			return URLEncoder.encode(str + "", "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public QPattern toQPattern(boolean fullMatch) {
		QPattern qp = new QPattern(RCharInner.this);
		qp.setFullMatch(fullMatch);
		return qp;
	}

	public int getMin() {
		return min;
	}

	public void setRange(int min, int max) {
		this.min = min;
		this.max = max;
	}
}
