package com.bmtech.spider.ext.scorer.scorers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.log.LogHelper;

public class MultiScorer {
	private static MultiScorer instance = new MultiScorer();

	public static MultiScorer getInstance() {
		return instance;
	}

	private final LogHelper log;

	private final File cfgFile = new File("./config/ws/scorer.conf");
	private Map<String, StringScorer> strScorerMap = new HashMap<String, StringScorer>();
	final List<RegexScorer> regexScorers = new ArrayList<RegexScorer>();
	WordCounter wordCounter;

	private MultiScorer() {
		try {
			log = new LogHelper("score");

			init();
			File wordsFile = new File(".tmp.wordCnt.txt");
			LineWriter lw = new LineWriter(wordsFile, false, Charsets.UTF8_CS);
			Set<String> strs = this.strScorerMap.keySet();
			for (String str : strs) {
				lw.writeLine(str);
			}
			lw.close();
			wordCounter = new WordCounter(wordsFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void init() throws IOException {
		List<String> confSections = ConfigReader
				.listSectionsInConfigFile(cfgFile);
		for (String section : confSections) {
			if (!section.startsWith("scorer_")) {
				continue;
			}
			log.info("load config %s", section);

			ScorerConfig conf = loadConf(section);
			if (section.startsWith("scorer_expex_")) {
				List<ScorerItem> lst = conf.toList(true);
				for (ScorerItem si : lst) {
					regexScorers.add((RegexScorer) si);
				}

			} else {
				List<ScorerItem> scorerList = conf.toList(false);
				for (ScorerItem si : scorerList) {
					StringScorer ss = strScorerMap.get(si.exp);
					if (ss == null || ss.score < si.score) {
						this.strScorerMap.put(si.exp, (StringScorer) si);
					}
				}
			}
		}
	}

	private ScorerConfig loadConf(String section) throws IOException {
		ConfigReader cr = new ConfigReader(cfgFile, section);
		return new ScorerConfig(cr);
	}

	public int score(String linex) {
		String line = linex.trim().toLowerCase();

		int total = this.scoreRegExItems(line) + this.scoreStrItems(line);
		return Math.min(total, ScoredUrlRecord.maxPageScore);
	}

	public int scoreRegExItems(String line) {
		int score = 0;
		for (RegexScorer regexScore : this.regexScorers) {
			score += regexScore.score(line);
		}
		return score;
	}

	public int scoreStrItems(String line) {
		Counter<String> cnt = wordCounter.wordCount(line);
		List<Entry<String, NumCount>> list = cnt.entryList();
		int score = 0;
		for (Entry<String, NumCount> a : list) {
			StringScorer ss = this.strScorerMap.get(a.getKey());
			if (ss == null) {
				continue;
			}
			score += ss.score(a.getValue().intValue());
		}
		return score;
	}

	public static void main(String[] args) throws IOException {
		MultiScorer sc = new MultiScorer();
		System.out.println(sc.regexScorers);
		System.out.println(sc.strScorerMap);
		while (true) {
			String source = Consoler.readString(":");
			System.out.println(sc.wordCounter.wordCount(source));
		}
	}
}
