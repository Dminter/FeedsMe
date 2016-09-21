package com.zncm.feedsme.utils;

import java.util.HashMap;
import java.util.Map;

public class Feeds {
	public static Map<String, String> typeMap = null;
	static {
		typeMap = new HashMap<String, String>();
		typeMap.put("leiphone", "http://www.leiphone.com/feed");
		typeMap.put("cnbeta",
				"http://cnbeta.feedsportal.com/c/34306/f/624776/index.rss");
		typeMap.put("newsmth", "http://www.newsmth.net/pc/rssrec.php");
		typeMap.put("163_rss_newstop",
				"http://news.163.com/special/00011K6L/rss_newstop.xml");
		typeMap.put("36kr", "http://www.36kr.com/feed");
		typeMap.put("feedburner", "http://feeds.feedburner.com/jandan");
		typeMap.put("cnblogs", "http://feed.cnblogs.com/blog/sitehome/rss");
		typeMap.put("techweb", "http://www.techweb.com.cn/rss/focus.xml");
		typeMap.put("sinatech", "	http://rss.sina.com.cn/news/allnews/tech.xml");
		typeMap.put("news_163",
				"http://news.163.com/special/00011K6L/rss_sh.xml");
		typeMap.put("feedsky", "http://feed.feedsky.com/qiushi");
		typeMap.put("appinn", "http://feed.appinn.com/");

		typeMap.put("u148", "http://www.u148.net/rss/");

		typeMap.put("huxiu", "http://www.huxiu.com/rss/0.xml");// 虎嗅网
		typeMap.put("yunkeji", "http://www.yunkeji.com/?feed=rss2");// 云科技
		typeMap.put("haha", "http://www.haha.mx/feed");// 哈哈.MX 分享所有好笑的事情
		typeMap.put("titan24", "http://www.titan24.com/rss.xml");// 体坛网-体坛周报
		typeMap.put("twwtn", "http://www.twwtn.com/xml/1.xml");// 科技世界网

		typeMap.put("chinanews_1",
				"http://www.chinanews.com/rss/scroll-news.xml");// 即时新闻
		typeMap.put("chinanews_2",
				"http://www.chinanews.com/rss/importnews.xml");// 要闻导读
		typeMap.put("chinanews_3", "http://www.chinanews.com/rss/china.xml");// 国内新闻
		typeMap.put("chinanews_4", "http://www.chinanews.com/rss/society.xml");// 社会新闻
		typeMap.put("chinanews_5", "http://www.chinanews.com/rss/it.xml");// IT

		typeMap.put("qq_1", "http://news.qq.com/newsgn/rss_newsgn.xml");// 国内新闻

		// 新闻
		typeMap.put("baidu_1",
				"http://news.baidu.com/n?cmd=4&amp;class=civilnews&amp;tn=rss");
		typeMap.put("baidu_2",
				"http://news.baidu.com/n?cmd=4&amp;class=internews&amp;tn=rss");
		typeMap.put("naivix_1", "http://www.naivix.com/world/rss.xml");
		typeMap.put("naivix_2", "http://www.naivix.com/china/rss.xml");
		typeMap.put("naivix_3", "http://www.naivix.com/shehui/rss.xml");
		typeMap.put("ycwb", "http://rss.ycwb.com/rss_jryw.xml");
		typeMap.put("hifiwiki", "http://www.hifiwiki.net/news/rss/infzm.xml");

		// 有趣
		typeMap.put("guokr_fun", "http://www.guokr.com/rss/");
		typeMap.put("doubanRss_fun", "http://feed.feedsky.com/doubanRss");
		typeMap.put("jandan_fun", "http://jandan.net/feed");
		typeMap.put("feedsky_fun", "http://feed.feedsky.com/qiushi");
		typeMap.put("myfreeheart_fun", "http://www.myfreeheart.cn/xml/Rss.xml");
		typeMap.put("163follow_fun", "http://163follow.blog.163.com/rss/");
		typeMap.put("jeequ_fun", "http://feed.jeequ.com/");
		typeMap.put("laifu1_fun", "http://www.laifu.org/rssfeed.xml");
		typeMap.put("laifu2_fun", "http://www.laifu.org/rssfeed_tupian.xml");

		// it
		typeMap.put("rssdiy_it", "http://rssdiy.com/rss.php?img&amp;drt");
		typeMap.put("engadget_it", "http://cn.engadget.com/rss.xml");
		typeMap.put("guokr_it", "http://www.guokr.com/rss/");
		typeMap.put("songshuhui_it", "http://songshuhui.net/feed");
		typeMap.put("xbeta_it", "http://feed.xbeta.info/");
		typeMap.put("appinn_it", "http://www.appinn.com/feed/");
		typeMap.put("feedsky_it", "http://feed.feedsky.com/showeb20");
		typeMap.put("iplaysoft_it", "http://feed.iplaysoft.com/");

		// sports
		typeMap.put("naivix_sports", "http://www.naivix.com/sports/rss.xml");
		typeMap.put("sina_sports",
				"http://rss.sina.com.cn/roll/sports/hot_roll.xml");
		typeMap.put("hoopchina_sports", "http://news.hoopchina.com/news.xml");
		// 编程
		typeMap.put("cnblogs_programme",
				"http://feed.cnblogs.com/blog/sitehome/rss");
		typeMap.put("oschina1_programme", "http://www.oschina.net/news/rss");
		typeMap.put("oschina2_programme", "http://www.oschina.net/project/rss");

		typeMap.put("iteye1_programme", "http://www.iteye.com/rss/blogs");
		typeMap.put("iteye2_programme",
				"http://www.iteye.com/rss/magazines/digest");
		typeMap.put("iteye3_programme", "http://www.iteye.com/rss/board/web");
		typeMap.put("iteye4_programme", "http://www.iteye.com/rss/board/mobile");
		typeMap.put("iteye5_programme", "http://www.iteye.com/rss/board/Job");
		typeMap.put("iteye6_programme", "http://www.iteye.com/rss/board/Java");
		typeMap.put("iteye7_programme", "http://www.iteye.com/rss/forum");

	}

}
