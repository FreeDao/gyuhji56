package textmatchers.pattern.def;

public abstract class QResult {

	/**
	 * get All match result count
	 * 
	 * @return
	 */
	public abstract int getTotalMatch();

	/**
	 * test if matched exist
	 * 
	 * @return {@link #getTotalMatch()} > 0
	 */
	public final boolean isMatch() {
		return this.getTotalMatch() > 0;
	}

	/**
	 * get match tree with index idx
	 * 
	 * @param idx
	 * @return
	 */
	protected abstract MatchNode getMatch(int matchedIndex);

	private int bestMatchedResultIndex = -1;

	protected int getBestMatchedResultIndex() {
		if (this.bestMatchedResultIndex == -1) {
			this.bestMatchedResultIndex = this.getBestMatchIndexByLength();
		}
		return this.bestMatchedResultIndex;
	}

	public final QResult getSubResult(int matcherIdx) {
		return this.getSubResult(this.getBestMatchedResultIndex(), matcherIdx);
	}

	private final QResult getSubResult(int matchedIndex, int matcherIdx) {
		MatchNode mnode = getMatch(matchedIndex);

		while (mnode.depth >= 0) {
			if (mnode.depth == matcherIdx)
				return mnode.toMResult();
			else
				mnode = mnode.leftNode;
		}
		return null;
	}

	public int getSubNodeNumber() {
		return this.getSubNodeNumber(this.getBestMatchedResultIndex());
	}

	private int getSubNodeNumber(int matchedIndex) {
		MatchNode mnode = getMatch(matchedIndex);
		return mnode.depth + 1;
	}

	public final MatchNode getSubNode(int matcherIdx) {
		return this.getSubNode(this.getBestMatchedResultIndex(), matcherIdx);
	}

	private final MatchNode getSubNode(int matchedIndex, int matcherIdx) {
		MatchNode mnode = getMatch(matchedIndex);

		while (true) {
			if (mnode == null) {
				return null;
			}
			if (mnode.depth < 0) {
				break;
			}
			if (mnode.depth == matcherIdx)
				return mnode;
			else
				mnode = mnode.leftNode;
		}
		return null;
	}

	public final int getMatchEnd() {
		return this.getMatchEnd(this.getBestMatchedResultIndex());
	}

	protected final int getMatchEnd(int idx) {
		return getMatch(idx).end();
	}

	public abstract void removeMatch(int idx);

	protected final int getBestMatchIndexByLength() {

		int ret = 0, max = 0;
		for (int x = 0; x < this.getTotalMatch(); x++) {
			int now = this.getMatchEnd(x);
			if (now > max) {
				max = now;
				ret = x;
			}
		}
		return ret;

	}

	public final int getMaxLen() {
		int index = this.getBestMatchedResultIndex();
		return this.getMatchEnd(index);
	}

	protected final String matchedResult(int index, String toMatch) {
		return toMatch.substring(0, this.getMatchEnd(index));
	}

	public final String matchedResult(String toMatch) {
		return matchedResult(this.getBestMatchedResultIndex(), toMatch);
	}

	public final String matchInfo(String toMatch) {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < this.getTotalMatch(); x++) {
			if (sb.length() > 0)
				sb.append('\n');
			int next = this.getMatchEnd(x);
			sb.append("0-" + next + ":" + toMatch.substring(0, next));
		}
		return sb.toString();
	}

	/**
	 * return matched str with index idx
	 * 
	 * @param idx
	 *            the matched result's index
	 * @param toMatch
	 * @return
	 */
	private final String getSubMatched(int idx, int matcherIdx, String toMatch) {
		MatchNode mnode = this.getSubNode(idx, matcherIdx);
		return toMatch.substring(mnode.offset, mnode.end());
	}

	public final String getSubMatched(int matcherIdx, String toMatch) {
		return this.getSubMatched(this.getBestMatchedResultIndex(), matcherIdx,
				toMatch);
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < this.getTotalMatch(); x++) {
			int next = this.getMatchEnd(x);
			sb.append("0-" + next + "\n");
		}
		return sb.toString().trim();
	}

}
