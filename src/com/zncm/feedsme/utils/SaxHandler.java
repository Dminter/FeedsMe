package com.zncm.feedsme.utils;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.zncm.feedsme.pojo.News;

public class SaxHandler extends DefaultHandler2 {
	News news;
	ArrayList<News> news_list;
	int current = 0;
	boolean is_description = false;

	public ArrayList<News> getNews_list() {
		return news_list;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		news_list = new ArrayList<News>();
		news = new News();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (localName.equals("channel")) {
			current = 0;
			return;
		}
		if (localName.equals("item")) {
			news = new News();
			return;
		}

		if (localName.equals("title")) {
			current = Constants.Feed_title;
			return;
		}
		if (localName.equals("description")) {
			if (is_description == true) {
				current = Constants.Feed_content;
				return;
			} else {
				is_description = true;
				return;
			}
		}
		if (localName.equals("link")) {
			current = Constants.Feed_links;
			return;
		}
		if (localName.equals("pubDate")) {
			current = Constants.Feed_pubdate;
			return;
		}
		current = 0;

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (localName.equals("item")) {
			news_list.add(news);
			return;
		}

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (length <= 5)
			return;
		String theString = new String(ch, start, length);

		switch (current) {
		case Constants.Feed_title:
			news.setTitle(CommonUtil.stripHtml(theString));
			current = 0;
			break;
		case Constants.Feed_links:
			news.setLinks(theString);
			current = 0;
			break;
		case Constants.Feed_content:
			if (is_description == true) {
				news.setContent(CommonUtil.stripHtml(theString));
				current = 0;
			} else {
				is_description = true;
			}
			break;
		case Constants.Feed_pubdate:
			news.setPubdate(theString);
			current = 0;
			break;
		default:
			return;
		}

	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

}
