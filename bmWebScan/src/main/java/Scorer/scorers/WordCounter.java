package Scorer.scorers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.bmtech.utils.ConfUtils;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.segment.Segment;
import com.bmtech.utils.segment.TokenHandler;

public class WordCounter extends Segment {
	private final HashMap<String, Short> map = new HashMap<String, Short>();
	private final boolean reverse = false;
	final int maxWord = 8;

	public WordCounter() {
		load(ConfUtils.getConfFile("segment/lexicon"), map, reverse, maxWord);
	}

	public WordCounter(File lexicon) {
		load(lexicon, map, reverse);
	}

	@Override
	public TokenHandler segment(String source) {
		return Segment.segment(source, map, reverse);
	}

	public Counter<String> wordCount(String source) {
		int len = source.length();
		int end = source.length() - 1;
		Counter<String> cnt = new Counter<String>();
		for (int index = 0; index < end; index++) {
			for (int offset = 2; offset < maxWord; offset++) {
				int endIndex = index + offset;
				if (endIndex > len) {
					break;
				}
				String str = source.substring(index, endIndex);
				Short type = map.get(str);
				if (type == null) {
					break;
				} else if (type == state_can) {
					cnt.count(str);
				} else if (type == state_is) {
					cnt.count(str);
					break;
				} else if (type == state_may) {
					continue;
				}
			}

		}
		return cnt;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		WordCounter instance = new WordCounter();
		while (true) {
			String ipt = Consoler.readString("\n:");
			System.out.println(instance.wordCount(ipt));
		}
	}
}
