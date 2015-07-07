//package textMatcher.Pattern.def;
//
//public interface QResultInterface {
//
//	/**
//	 * get All match result count
//	 * @return
//	 */
//	public int getTotalMatch();
//	
//	/**
//	 * test if matched exist
//	 * @return {@link #getTotalMatch()} > 0
//	 */
//	public boolean isMatch();
//	
//	/**
//	 * get match tree with index idx
//	 * @param idx
//	 * @return
//	 */
//	public String getSubMatch(int idx, int matcherIdx, String toMatch);
//	
//	/**
//	 * get the match length for match with index idx
//	 * @param idx
//	 * @return
//	 */
//	public int getMatchEnd(int idx);
//	
//	public void removeMatch(int idx);
//
//	public int getMaxLen();
//
//	public String matchInfo(String toMatch);
//	/**
//	 * return matched str with index idx
//	 * @param idx the matched result's index
//	 * @param toMatch
//	 * @return
//	 */
//	public String getMatched(int idx, int matcherIdx, String toMatch);
//	
//	/**
//	 * get match tree with index idx
//	 * @param idx
//	 * @return
//	 */
//	public MatchNode getMatch(int idx);
//
//}
