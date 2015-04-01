package com.bmtech.spider.core;
public class LinkClass{
	public final String text;
	private String linkIner;
	public final String title;
	LinkClass(String text, String title, String link){
		this.text = text == null? "" : text.trim();
		this.title = title == null? "" : title.trim();
		setLink(link);
	}
	public void setLink(String link) {
		this.linkIner = link == null? "" : link.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ').trim();
	}
	public String getLink(){
		return this.linkIner;
	}
}