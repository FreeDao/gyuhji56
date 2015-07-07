package frameParser.titleExt.art;

import java.util.ArrayList;

import frameParser.titleExt.SerialNumber;

public class ArtInfo {
	public static ArtInfo getArtInfoFromText(String text) {
		// char[] cs = text.toCharArray();
		// StringBuilder sb = new StringBuilder();
		// for(char c : cs){
		// if(c == ':' || c == '：'){
		// sb.append(':');
		// sb.append('\n');
		// }else{
		// sb.append(c);
		// }
		// }
		// text = sb.toString().trim();
		String[] lines = text.trim().split("\n");
		return getArtInfoFromLines(lines);
	}

	public static ArtInfo getArtInfoFromLines(String lines[]) {
		return new ArtInfo(lines);
	}

	ArtSegment textBranch;
	ArtBranch bhBranch;

	public final ArrayList<ArtLine> lines = new ArrayList<ArtLine>();
	private ArrayList<ArtSegment> segments = new ArrayList<ArtSegment>();

	private ArtInfo(String[] lineS) {
		ArtLine left = null;
		for (String line : lineS) {
			SerialNumber tn = null;
			ArrayList<SerialNumber> tns = SerialNumber.parseNum(line);
			if (tns.size() > 0) {
				tn = tns.get(0);
			}
			ArtLine al = new ArtLine(line, tn, left);
			left = al;
			lines.add(al);
		}
		findBranchs();

	}

	class InLinePattern {
		String line;
		int start;
		SerialNumber value;

		InLinePattern(String line, int start, SerialNumber value) {
			this.line = line;
			this.start = start;
			this.value = value;
		}
	};

	ArrayList<InLinePattern> serachLineNum(ArrayList<String> lines,
			SerialNumber now) {
		String headLine = lines.get(0);
		InLinePattern snum = new InLinePattern(headLine, 0, now);
		ArrayList<InLinePattern> foundGood = new ArrayList<InLinePattern>();
		foundGood.add(snum);
		for (String x : lines) {
			searchLineNum(x, foundGood);
		}
		foundGood.remove(0);
		return foundGood;
	}

	void searchLineNum(String line, ArrayList<InLinePattern> foundGood) {
		SerialNumber lastValue = foundGood.get(foundGood.size() - 1).value;
		SerialNumber nextValue = lastValue.next();
		String value = nextValue.strValue();
		int nextNum = foundGood.get(foundGood.size() - 1).start
				+ lastValue.strValue().length() + 3;
		if (nextNum < line.length()) {
			int pos = line.indexOf(value, 3);
			if (pos == -1) {
				return;
			} else {// FIXME should get a better value
				foundGood.add(new InLinePattern(line, pos, nextValue));
			}
		} else {
			return;
		}
	}

	private void findBranchs() {

		ArtSegment crtSegment = null;
		SerialNumber crtNum = null;
		for (int x = 0; x < lines.size(); x++) {// find 1 indexes
			ArtLine aline = lines.get(x);
			if (x == 0) {// first line
				crtSegment = new ArtSegment(aline);
				segments.add(crtSegment);
				crtNum = aline.tNum;
				continue;
			}
			boolean canMerge = false;
			if (aline.tNum == null) {
				canMerge = true;
			} else {
				if (crtNum != null) {
					SerialNumber nextNum = crtNum.next();
					if (nextNum.equals(aline.tNum)) {
						canMerge = true;
					}
				}
			}
			if (canMerge) {
				crtSegment.addArtLine(aline);
				if (aline.tNum != null) {
					crtNum = aline.tNum;
				}
			} else {
				crtSegment = new ArtSegment(aline);
				segments.add(crtSegment);
				crtNum = aline.tNum;
			}
		}
		if (segments.size() > 0) {
			ArtSegment ab = segments.get(0);
			if (ab.getHeadLine().tNum == null) {
				this.textBranch = ab;
				segments.remove(0);
			}
		}
		if (segments.size() == 0)
			return;
		// make root-branch
		this.bhBranch = new ArtBranch(segments.get(0), null);

		for (int x = 1; x < segments.size(); x++) {
			ArtSegment segment = segments.get(x);
			ArtSegment left = segments.get(x - 1);
			ArtLine head = segment.getHeadLine();
			SerialNumber tnum = head.tNum;
			if (tnum.intNum() == 1) {
				ArtBranch brc = new ArtBranch(segment, left);
				left.setBranchChild(brc);
			} else {
				ArtBranch toLinkTo = null;
				ArtSegment asg = left;
				while (true) {
					if (asg.myBranch.par != null) {
						ArtBranch test = asg.myBranch.par.myBranch;
						if (test.canAppend(segment)) {
							toLinkTo = test;
							break;
						}
						asg = asg.myBranch.par;
					} else {
						break;
					}
				}
				if (toLinkTo == null) {
					System.out.flush();
					System.err.println(segment);
					System.err.flush();
					toLinkTo = null;
					asg = left;
					while (true) {
						if (asg.myBranch.par != null) {
							ArtBranch test = asg.myBranch.par.myBranch;
							if (test.patternMatch(segment)) {
								if (test.canAppend(segment)) {
									toLinkTo = test;
									break;
								} else {
									if (test.getTail()
											.endNum()
											.lessThan(
													segment.getHeadLine().tNum)) {
										ArrayList<String> lines = new ArrayList<String>();
										ArrayList<ArtLine> alines = test
												.getTail().lines;
										for (ArtLine al : alines) {
											lines.add(al.line);
										}
										ArrayList<InLinePattern> serachLineNum = this
												.serachLineNum(lines, test
														.getTail().endNum());
										if (serachLineNum.size() > 0) {
											InLinePattern end = serachLineNum
													.get(serachLineNum.size() - 1);
											if (end.value.next().equals(
													segment.endNum())) {
												// TODO toMerge
												System.err
														.println("to link "
																+ test + "--"
																+ segment);
												break;
											}
										}
									}
								}
							}
							asg = asg.myBranch.par;
						} else {
							break;
						}
					}
				}
				if (toLinkTo != null) {
					toLinkTo.append(segment);
				} else {
					ArtBranch brc = new ArtBranch(segment, left);
					left.setBranchChild(brc);
				}

			}
		}
	}

	private String getWhiteSpace(int xx) {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < xx; x++) {
			sb.append('\t');
		}
		return sb.toString();
	}

	private void explain(ArtBranch branch, int depth) {
		if (branch == null)
			return;
		String prf = this.getWhiteSpace(depth);
		for (ArtSegment seg : branch.segments) {
			for (ArtLine al : seg.lines) {
				if (al.tNum == null) {
					System.out.print("\t");
				}
				System.out.println(prf + "" + al);
			}
			if (seg.myChild != null) {
				explain(seg.myChild, depth + 1);
			}
		}
	}

	public void explain() {
		System.out.println("ArtSegment:" + textBranch);
		explain(this.bhBranch, 1);
	}

}
