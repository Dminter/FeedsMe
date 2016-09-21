package com.zncm.feedsme.utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.zncm.feedsme.pojo.News;

public class FeedsUtils {

	public static ArrayList<News> GetNews(String type) {
		try {
			ArrayList<News> news_list = new ArrayList<News>();
			String urlString = Feeds.typeMap.get(type);
			URL url = new URL(urlString);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();
			SaxHandler rssHandler = new SaxHandler();
			xmlReader.setContentHandler(rssHandler);
			InputSource is = new InputSource(url.openStream());
			xmlReader.parse(is);
			news_list = rssHandler.getNews_list();
			DownCurrentNews(news_list, type);
			return news_list;
		} catch (Exception e) {
			return GetNewsByJsoup(type);
		}
	}

	public static String SaveNews(String type) {
		Document doc;
		String url = Feeds.typeMap.get(type);
		ArrayList<News> news_list = new ArrayList<News>();
		News news = null;
		try {
			doc = Jsoup.connect(url).timeout(1000).get();
			Elements title_elements = doc.select("item").select("title");
			Elements description_elements = doc.select("item").select(
					"description");
			Elements pubdate_elements = doc.select("item").select("pubdate");
			Elements item_elements = doc.select("item");
			int totalSize = item_elements.size();
			for (int i = 0; i < totalSize; i++) {
				news = new News();
				String temp1 = item_elements.get(i).html();
				String temp2 = temp1.substring(temp1.indexOf("<link />") + 8);
				String temp3 = temp2.substring(0, temp2.indexOf("<"));
				news.setTitle(CommonUtil
						.stripHtml(title_elements.get(i).text()));
				news.setContent(CommonUtil.stripHtml(description_elements
						.get(i).text()));
				news.setPubdate(pubdate_elements.get(i).text());
				news.setLinks(temp3);
				news_list.add(news);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JsonUtils.toJson(news_list);
	}

	public static ArrayList<News> GetNewsByJsoup(String type) {
		Document doc;
		String url = Feeds.typeMap.get(type);
		ArrayList<News> news_list = new ArrayList<News>();
		News news = null;
		try {
			doc = Jsoup.connect(url).timeout(1000).get();
			Elements title_elements = doc.select("item").select("title");
			Elements description_elements = doc.select("item").select(
					"description");
			Elements pubdate_elements = doc.select("item").select("pubdate");
			Elements item_elements = doc.select("item");
			int totalSize = item_elements.size();
			for (int i = 0; i < totalSize; i++) {
				news = new News();
				String temp1 = item_elements.get(i).html();
				String temp2 = temp1.substring(temp1.indexOf("<link />") + 8);
				String temp3 = temp2.substring(0, temp2.indexOf("<"));
				news.setTitle(CommonUtil
						.stripHtml(title_elements.get(i).text()));
				news.setContent(CommonUtil.stripHtml(description_elements
						.get(i).text()));
				news.setPubdate(pubdate_elements.get(i).text());
				news.setLinks(temp3);
				news_list.add(news);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		DownCurrentNews(news_list, type);
		return news_list;
	}

	private static void DownCurrentNews(ArrayList<News> news_list, String type) {
		JsonUtils.SaveJsonToSDCard(type, JsonUtils.toJson(news_list));
	}

}
