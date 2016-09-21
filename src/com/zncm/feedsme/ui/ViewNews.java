package com.zncm.feedsme.ui;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zncm.feedsme.pojo.News;
import com.zncm.feedsme.utils.CommonUtil;
import com.zncm.feedsme.utils.SPUtils;

public class ViewNews extends Activity implements OnClickListener {

	SPUtils spUtils;

	TextView t_tv01 = null;
	TextView t_tv00 = null;
	ImageView t_iv01;

	ImageView b_iv01, b_iv02, b_iv03, b_iv04;
	TextView f_tv01, f_tv02 = null;
	LinearLayout f_ll01;

	LinearLayout v_ll01;

	LinearLayout s_ll01;
	TextView s_tv01, s_tv02 = null;
	int current = 0;

	ArrayList<News> news_list = null;
	private ViewPager vp01;
	private ArrayList<View> pageViews;
	ScrollView scrollView[];
	LinearLayout temp_layout[];
	TextView title_tv[];
	TextView content_tv[];
	TextView pubdate_tv[];
	WebView wv[];
	View view[];

	int max_f_size = 40, min_f_size = 10;

	int content_font_size = 16;
	boolean is_font_setting = true;
	boolean is_link_show = true;
	boolean is_send_show = true;
	boolean is_night_show = true;

	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {

			InputStream is = null;
			try {
				is = new URL(source).openStream();
				Drawable d = Drawable.createFromStream(is, "");
				d.setBounds(0, 0, CommonUtil.getWH(ViewNews.this)[0],
						CommonUtil.getWH(ViewNews.this)[1]);
				is.close();
				return d;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

	};

	void initViews(int totalSize) {

		title_tv = new TextView[totalSize];
		content_tv = new TextView[totalSize];
		pubdate_tv = new TextView[totalSize];
		view = new View[totalSize];
		temp_layout = new LinearLayout[totalSize];
		scrollView = new ScrollView[totalSize];
		wv = new WebView[totalSize];
		pageViews = new ArrayList<View>();
		for (int i = 0; i < totalSize; i++) {
			title_tv[i] = new TextView(this);
			title_tv[i].setText(news_list.get(i).getTitle());
			title_tv[i].setTextSize(25);
			title_tv[i].setMaxLines(2);
			title_tv[i].setGravity(Gravity.CENTER);
			content_tv[i] = new TextView(this);
			content_tv[i].setText("		" + news_list.get(i).getContent());
			Linkify.addLinks(content_tv[i], Linkify.WEB_URLS);
			content_tv[i].setTextSize(content_font_size);
			pubdate_tv[i] = new TextView(this);
			if (news_list.get(i).getPubdate() != null
					&& !news_list.get(i).getPubdate().equals("null")
					&& (news_list.get(i).getPubdate().contains("CST")
							|| news_list.get(i).getPubdate().contains("GMT")
							|| news_list.get(i).getPubdate().contains("+") || news_list
							.get(i).getPubdate().contains("EST"))) {
				pubdate_tv[i].setText(CommonUtil.getDisplayDate(new Date(
						news_list.get(i).getPubdate())));
			} else if (news_list.get(i).getPubdate() != null
					&& !news_list.get(i).getPubdate().equals("null")) {
				pubdate_tv[i].setText(news_list.get(i).getPubdate());
			} else {
				pubdate_tv[i].setText(CommonUtil.getDisplayDate(new Date()));
			}

			pubdate_tv[i].setGravity(Gravity.RIGHT);
			pubdate_tv[i].setTextSize(15);
			wv[i] = new WebView(this);
			wv[i].setVisibility(View.GONE);
			wv[i].getSettings().setJavaScriptEnabled(true);
			wv[i].getSettings().setBuiltInZoomControls(true);
			wv[i].getSettings().setSupportZoom(true);

			wv[i].setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {

					view.getSettings().setJavaScriptEnabled(true);
					view.loadUrl(url);

					view.requestFocus();

					return true;
				}
			});

			// content_tv[i].setMovementMethod(ScrollingMovementMethod
			// .getInstance());
 
			temp_layout[i] = new LinearLayout(this);
			temp_layout[i].setOrientation(LinearLayout.VERTICAL);
			temp_layout[i].addView(title_tv[i]);
			temp_layout[i].addView(pubdate_tv[i]);
			temp_layout[i].addView(content_tv[i]);
			temp_layout[i].addView(wv[i]);

			temp_layout[i].setPadding(20, 5, 20, 20);
			if (spUtils.getNight()) {
				title_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
				content_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
				pubdate_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
				v_ll01.setBackgroundColor(R.color.night);
			} else {
				title_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
				content_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
				pubdate_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
				v_ll01.setBackgroundColor(Color.WHITE);
			}
			scrollView[i] = new ScrollView(this);
			scrollView[i].addView(temp_layout[i]);
			pageViews.add(scrollView[i]);
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_news);
		v_ll01 = (LinearLayout) findViewById(R.id.view_news_ll00);
		s_ll01 = (LinearLayout) findViewById(R.id.view_news_lay1);
		s_tv01 = (TextView) s_ll01.findViewById(R.id.send_to_tv01);
		s_tv02 = (TextView) s_ll01.findViewById(R.id.send_to_tv02);
		s_tv01.setOnClickListener(this);
		s_tv02.setOnClickListener(this);

		f_ll01 = (LinearLayout) findViewById(R.id.view_news_lay2);
		f_tv01 = (TextView) f_ll01.findViewById(R.id.font_size_tv01);
		f_tv02 = (TextView) f_ll01.findViewById(R.id.font_size_tv02);
		f_tv01.setOnClickListener(this);
		f_tv02.setOnClickListener(this);

		LinearLayout ll01 = (LinearLayout) findViewById(R.id.view_news_lay3);
		b_iv01 = (ImageView) ll01.findViewById(R.id.tools_bar_iv01);
		b_iv02 = (ImageView) ll01.findViewById(R.id.tools_bar_iv02);
		b_iv03 = (ImageView) ll01.findViewById(R.id.tools_bar_iv03);
		b_iv04 = (ImageView) ll01.findViewById(R.id.tools_bar_iv04);
		b_iv01.setOnClickListener(this);
		b_iv02.setOnClickListener(this);
		b_iv03.setOnClickListener(this);
		b_iv04.setOnClickListener(this);

		RelativeLayout rl01 = (RelativeLayout) findViewById(R.id.view_news_lay);
		t_tv01 = (TextView) rl01.findViewById(R.id.common_title_tv01);
		t_tv00 = (TextView) rl01.findViewById(R.id.common_title_tv00);
		t_iv01 = (ImageView) rl01.findViewById(R.id.common_title_iv01);
		t_iv01.setVisibility(View.VISIBLE);
		t_iv01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		String title = getIntent().getExtras().getString("title");
		String content = getIntent().getExtras().getString("content");
		String paper = getIntent().getExtras().getString("paper");
		current = getIntent().getExtras().getInt("current");
		t_tv01.setText(paper);

		ArrayList list = getIntent().getExtras().getParcelableArrayList(
				"news_list");
		news_list = (ArrayList<News>) list.get(0);

		vp01 = (ViewPager) findViewById(R.id.view_news_vp01);
		spUtils = new SPUtils(ViewNews.this);

		is_night_show = spUtils.getNight();
		content_font_size = spUtils.getFont_size();
		initViews(news_list.size());
		if (is_night_show) {
			title_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
			content_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
			pubdate_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
			v_ll01.setBackgroundColor(R.color.night);
		} else {
			title_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
			content_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
			pubdate_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
			v_ll01.setBackgroundColor(Color.WHITE);
		}
		vp01.setAdapter(new PagerView());
		vp01.setCurrentItem(current);
		vp01.clearAnimation();

	}

	class PagerView extends PagerAdapter {
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {

			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			t_tv00.setText(vp01.getCurrentItem() + 1 + "/" + pageViews.size());
			content_tv[vp01.getCurrentItem()].setTextSize(content_font_size);

			if (spUtils.getNight()) {
				title_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
				content_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
				pubdate_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
				v_ll01.setBackgroundColor(R.color.night);
			} else {
				title_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
				content_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
				pubdate_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
				v_ll01.setBackgroundColor(Color.WHITE);
			}
			if (content_tv[vp01.getCurrentItem()].getText().toString()
					.contains("img")) {

				URLSpan[] urlSpan = content_tv[vp01.getCurrentItem()].getUrls();
				if (urlSpan.length > 0) {
					content_tv[vp01.getCurrentItem()].setText(Html.fromHtml(
							"<img src='" + urlSpan[0].getURL() + "'>",
							imageGetter, null));
				}
			}

			return pageViews.get(arg1);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tools_bar_iv01:
			if (is_link_show) {
				wv[vp01.getCurrentItem()].loadUrl(news_list.get(
						vp01.getCurrentItem()).getLinks());
				wv[vp01.getCurrentItem()].setVisibility(View.VISIBLE);
				is_link_show = false;
			} else {
				wv[vp01.getCurrentItem()].setVisibility(View.GONE);
				is_link_show = true;
			}

			break;
		case R.id.tools_bar_iv02:
			if (is_font_setting) {
				f_ll01.setVisibility(View.VISIBLE);
				is_font_setting = false;
			} else {

				f_ll01.setVisibility(View.GONE);
				is_font_setting = true;
			}
			break;
		case R.id.tools_bar_iv03:
			if (is_send_show) {
				s_ll01.setVisibility(View.VISIBLE);
				is_send_show = false;
			} else {

				s_ll01.setVisibility(View.GONE);
				is_send_show = true;
			}
			break;
		case R.id.tools_bar_iv04:
			if (is_night_show) {
				title_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
				content_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
				pubdate_tv[vp01.getCurrentItem()].setTextColor(Color.WHITE);
				v_ll01.setBackgroundColor(R.color.night);
				spUtils.setNight(is_night_show);
				is_night_show = false;
			} else {
				title_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
				content_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
				pubdate_tv[vp01.getCurrentItem()].setTextColor(Color.BLACK);
				v_ll01.setBackgroundColor(Color.WHITE);
				spUtils.setNight(is_night_show);
				is_night_show = true;
			}
			break;
		case R.id.font_size_tv01:
			if (content_font_size < max_f_size) {
				content_font_size += 2;
				content_tv[vp01.getCurrentItem()]
						.setTextSize(content_font_size);
				spUtils.setFont_size(content_font_size);
			}
			break;
		case R.id.font_size_tv02:
			if (content_font_size > min_f_size) {
				content_font_size -= 2;
				content_tv[vp01.getCurrentItem()]
						.setTextSize(content_font_size);
				spUtils.setFont_size(content_font_size);
			}
			break;
		case R.id.send_to_tv01:
			CommonUtil.copyText(ViewNews.this,
					content_tv[vp01.getCurrentItem()].getText().toString());
			break;

		case R.id.send_to_tv02:
			CommonUtil.sendSMS(ViewNews.this,
					news_list.get(vp01.getCurrentItem()).getTitle()
							+ news_list.get(vp01.getCurrentItem()).getLinks());
			break;

		default:
			break;
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}