package textmatchers.pattern.def.reg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import textmatchers.pattern.def.MString;
import textmatchers.pattern.def.MatchNode;
import textmatchers.pattern.def.QEnumResult;
import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.reg.AreaSeg.AreaSegment;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.segment.TokenHandler;

public class SArea implements QMatchable {
	private static AreaSegment __segIns;

	private static void init() {
		__segIns = AreaSeg.getInstance().segment;
		try {
			LineReader lr = new LineReader(new File(
					"config/segment/fullName.txt"), "utf8");
			while (lr.hasNext()) {
				String ln = lr.next();
				ln = ln.trim();
				if (ln.length() > 1) {
					__segIns.addItem(ln);
					if (ln.length() > 2) {
						if (ln.endsWith("区")) {
							__segIns.addItem(ln.substring(0, ln.length() - 1));
							// System.out.println("skip area segment word:" +
							// ln);
						}
					}
				} else {
					System.out.println("skip area segment word:" + ln);
				}
			}
			lr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public QEnumResult match(MString str) {
		ArrayList<Integer> end = new ArrayList<Integer>();
		HashSet<String> set = new HashSet<String>();

		TokenHandler hdl = getSegIns().segment(str.getStr());
		while (hdl.hasNext()) {
			String token = hdl.next();
			if (token.length() < 2)
				continue;
			set.add(token);
		}
		for (String x : set) {
			if (str.startsWith(x)) {
				end.add(x.length());
			}
		}
		QEnumResult mcd = new QEnumResult();
		for (int v : end) {
			mcd.addMatch(new MatchNode(0, v, this, null, 0, null));
		}
		return mcd;
	}

	@Override
	public String toString() {
		return "@" + this.getClass().getName();
	}

	public static void main(String[] a) {
		String vvv = "阿坝县财政局";
		TokenHandler hdl = getSegIns().segment(vvv);
		while (hdl.hasNext()) {
			String str = hdl.next();
			System.out.println(str);
		}

		SArea am = new SArea();
		System.out.println(am.match(new MString(vvv, 0)).matchInfo(vvv));
	}

	private synchronized static AreaSegment getSegIns() {
		if (__segIns == null) {
			init();
		}
		return null;
	}
}
