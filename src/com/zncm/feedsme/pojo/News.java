package com.zncm.feedsme.pojo;

import java.io.Serializable;

public class News implements Serializable {
	private String paper;
	private String title;
	private String content;
	private String pubdate;
	private String links;

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getPaper() {
		return paper;
	}

	public void setPaper(String paper) {
		this.paper = paper;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "News [paper=" + paper + ", title=" + title + ", content="
				+ content + "]";
	}

}
