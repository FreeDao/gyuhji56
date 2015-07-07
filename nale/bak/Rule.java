package textMatcher.Pattern.def;

public abstract class Rule extends QPattern {

	public final String name;

	protected Rule(String name) {
		this.name = name;
	}

	@Override
	public void setMatchers() {
		final QMatchable[] lst = defineRule();
		for (QMatchable qm : lst) {
			addMatcher(qm);
		}
		final QMatchable[] notMatchRule = defineNotRule();
		if (notMatchRule != null) {
			for (QMatchable qm : notMatchRule) {
				addNotMatcher(qm);
			}
		}
	}

	/**
	 * define rules
	 * 
	 * @return
	 */
	protected abstract QMatchable[] defineRule();

	public QMatchable[] defineNotRule() {
		return null;
	}

}
