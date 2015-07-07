package tv.fun.addon;

import java.io.File;

public class SemMatchInfo {
	private final SemToken semName;
	private final File matchedRule;

	public SemMatchInfo(String semName, File matchedRule) {
		this.semName = SemToken.toSemToken(semName);
		this.matchedRule = matchedRule;
	}

	public SemToken getSemName() {
		return semName;
	}

	public File getMatchedRule() {
		return matchedRule;
	}
}
