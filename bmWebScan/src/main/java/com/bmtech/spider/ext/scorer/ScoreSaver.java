package com.bmtech.spider.ext.scorer;

import java.net.URL;

public interface ScoreSaver {

	void save(String title, String lines, URL url, int score) throws Exception;

	void saveHostScore(String hostName, int[] infoArray) throws Exception;

}
