package textmatchers.pattern.def;

abstract class MatchableList {

	public abstract int size();

	public abstract QMatchable get(int matcherIdx);

	public abstract boolean canStop(int matcherIdx);

	public abstract boolean shouldExpandAndMatcher();

}