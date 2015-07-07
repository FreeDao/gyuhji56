package textmatchers.pattern.def;

import java.util.ArrayList;

import textmatchers.pattern.def.reg.RSZ;
import textmatchers.pattern.def.reg.SString;

public class BooleanAndMatcher implements QMatchable {
	QMatchable[] matcher;

	public BooleanAndMatcher(QMatchable... match) {
		this.matcher = match;
	}

	public BooleanAndMatcher(ArrayList<?> matches) {
		this.matcher = new QMatchable[matches.size()];
		for (int x = 0; x < matches.size(); x++) {
			matcher[x] = (QMatchable) matches.get(x);
		}
	}

	@Override
	public QResult match(MString str) {
		QPattern qp = new QPattern(this);

		return qp.match(str);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (QMatchable qm : this.matcher) {
			if (qm instanceof SString) {
				sb.append(qm);
			} else {
				sb.append("{");
				sb.append(qm);
				sb.append("}");
			}

		}
		return sb.toString();

	}

	public static void main(String[] a) {
		String x = "1234";
		RSZ rsz = new RSZ(2, 4);

		RSZ rsz1 = new RSZ(1);

		BooleanAndMatcher and = new BooleanAndMatcher(rsz, rsz1);
		QResult r = and.match(MString.newStr(x));
		System.out.println(r.matchInfo(x));
		System.out.println(r.getSubMatched(0, x));
		System.out.println(r.getSubMatched(1, x));
		System.out.println("---------------");
		System.out.println(r.getSubMatched(0, x));
		System.out.println(r.getSubMatched(1, x));
	}
}
