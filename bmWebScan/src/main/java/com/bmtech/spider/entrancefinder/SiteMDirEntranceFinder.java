package com.bmtech.spider.entrancefinder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Scorer.scorers.MultiScorer;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.util.SiteMDirReader;
import com.bmtech.spider.core.util.SynCombin.DecodeSynCombin;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.log.LogHelper;

public class SiteMDirEntranceFinder {

	MultiScorer scorer = MultiScorer.getInstance();
	ScanConfig sc = ScanConfig.instance;
	MDir mdir;
	LogHelper log;
	SiteMDirReader reader;
	HostInfo hi;

	SiteMDirEntranceFinder(MDir mdir, HostInfo hi) throws Exception {
		this.mdir = mdir;
		this.hi = hi;
		log = new LogHelper(mdir.dataFile.getName());
		reader = new SiteMDirReader(mdir);
	}

	public void caculate() throws Exception {
		while (reader.hasNext()) {
			DecodeSynCombin entry = reader.next();
			Parser p = new Parser(entry.html);
			NodeList nl = p.parse(null);
			NodeList links = nl.extractAllNodesThatMatch(new NodeClassFilter(
					LinkTag.class), true);
			for (int x = 0; x < links.size(); x++) {
				LinkTag lt = (LinkTag) links.elementAt(x);
				String sU = lt.getLink();
				try {
					URL url = new URL(entry.url, sU);
					if (hi.isMyUrl(url)) {
						countLinkOut(entry.url, url, lt.getLink());
					}
				} catch (Exception e) {
				}
			}
		}
	}

	private HashMap<String, PageInfo> map = new HashMap<String, PageInfo>();

	private void countLinkOut(URL from, URL to, String text) {

		if (text == null)
			text = "";
		text = text.toLowerCase().replace("&nbsp;", " ").replace("&nbsp", " ")
				.replace(" ", "");// .trim();
		double weight = 1.0;
		if (text.length() > 1) {
			weight = 1 + 4.0 / (2 + text.length());
		}
		int w = (int) (weight * 10);

		PageInfo piTo = getFromMap(to);
		piTo.addRefer(w);

		PageInfo pi = getFromMap(from);
		pi.addLinkOut(w);

	}

	private PageInfo getFromMap(URL url) {
		String s1 = url.toString();
		PageInfo pi = map.get(s1);
		if (pi == null) {
			pi = new PageInfo(url);
			map.put(s1, pi);
		}
		return pi;
	}

	public List<PageInfo> getByLinkOutNum() {
		List<PageInfo> list = new ArrayList<PageInfo>();
		list.addAll(map.values());
		Collections.sort(list, new Comparator<PageInfo>() {

			@Override
			public int compare(PageInfo o1, PageInfo o2) {
				return o2.getLinkOut() - o1.getLinkOut();
			}

		});

		return list;
	}

	public List<PageInfo> getByReferNum() {
		List<PageInfo> list = new ArrayList<PageInfo>();
		list.addAll(map.values());
		Collections.sort(list, new Comparator<PageInfo>() {

			@Override
			public int compare(PageInfo o1, PageInfo o2) {
				return o2.getRefer() - o1.getRefer();
			}

		});

		return list;
	}

	public List<PageInfo> getBySumNum() {
		List<PageInfo> list = new ArrayList<PageInfo>();
		list.addAll(map.values());
		Collections.sort(list, new Comparator<PageInfo>() {

			@Override
			public int compare(PageInfo o1, PageInfo o2) {
				return o2.getRefer() + o2.getLinkOut() - o1.getRefer()
						- o1.getLinkOut();
			}
		});
		return list;
	}

	class RoundRubin {
		Object[] arr = new Object[] { getByLinkOutNum().iterator(),
				getByLen().iterator(), getByReferNum().iterator(),
				getBySumNum().iterator() };
		int arrIndex = 0;
		int fail = 0;

		PageInfo next() {
			if (arrIndex >= arr.length) {
				arrIndex = 0;
			}
			Iterator<PageInfo> itr = (Iterator<PageInfo>) arr[arrIndex];
			arrIndex++;
			if (itr.hasNext()) {
				fail = 0;
				return itr.next();
			} else {
				fail++;
				if (fail == arr.length) {
					return null;
				}
				return next();
			}

		}
	}

	public List<PageInfo> top(int num) {

		List<PageInfo> set = new ArrayList<PageInfo>();
		RoundRubin rr = new RoundRubin();
		while (set.size() < num) {
			PageInfo next = rr.next();
			if (next == null) {
				break;
			}
			if (set.contains(next)) {
				continue;
			}
			set.add(next);
		}
		return set;
	}

	public List<PageInfo> getByLen() {
		List<PageInfo> list = new ArrayList<PageInfo>();
		list.addAll(map.values());
		Collections.sort(list, new Comparator<PageInfo>() {

			@Override
			public int compare(PageInfo o1, PageInfo o2) {
				return o1.getUrl().length() - o2.getUrl().length();
			}
		});

		return list;
	}

	public static void main(String[] args) throws Exception {
		File f = new File(
				"E:\\datas\\rl2ee\\okCrawled\\xiq.h\\xiqing.mofcom.gov.cn");
		MDir dir = MDir.open(f);

		SiteMDirEntranceFinder sc = new SiteMDirEntranceFinder(dir,
				new HostInfo(f.getName()));
		sc.caculate();
		List<PageInfo> list = sc.getByLinkOutNum();
		for (int x = 0; x < 100; x++) {
			PageInfo pi = list.get(x);
			System.out.println((1 + x) + "\t" + pi);
		}
		Consoler.readString("~~~~");
		list = sc.getByReferNum();
		for (int x = 0; x < 100; x++) {
			PageInfo pi = list.get(x);
			System.out.println((1 + x) + "\t" + pi);
		}

		Consoler.readString("~~~~");
		list = sc.getBySumNum();
		for (int x = 0; x < 100; x++) {
			PageInfo pi = list.get(x);
			System.out.println((1 + x) + "\t" + pi);
		}
		Consoler.readString("~~~~");
		list = sc.getByLen();
		for (int x = 0; x < 100; x++) {
			PageInfo pi = list.get(x);
			System.out.println((1 + x) + "\t" + pi);
		}
		Consoler.readString("?????");
		Collection<PageInfo> set = sc.top(20);
		for (PageInfo pi : set) {
			System.out.println("\t" + pi);
		}
	}
}
