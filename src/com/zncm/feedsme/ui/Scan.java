package com.zncm.feedsme.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zncm.feedsme.pojo.News;
import com.zncm.feedsme.utils.CommonUtil;
import com.zncm.feedsme.utils.JsonUtils;
import com.zncm.feedsme.utils.SPUtils;

public class Scan extends Activity {
	SPUtils spUtils;
	ArrayList<News> news_list = null;
	String key = "";
	GridViewAdapter gridViewAdapter = null;
	GridView gv01 = null;
	ProgressBar pb01 = null;
	TextView tv01 = null;
	TextView t_tv01 = null;
	ImageView t_iv01 = null;

	TextView g_tv01;
	LinearLayout ll01 = null;
	String type;
	String paper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		ll01 = (LinearLayout) findViewById(R.id.scan_ll01);

		type = getIntent().getExtras().getString("type");

		paper = getIntent().getExtras().getString("paper");
		gv01 = (GridView) findViewById(R.id.scan_gv01);
		pb01 = (ProgressBar) findViewById(R.id.scan_pb01);
		pb01.setVisibility(View.VISIBLE);
		tv01 = (TextView) findViewById(R.id.scan_tv01);
		tv01.setVisibility(View.VISIBLE);

		RelativeLayout rl01 = (RelativeLayout) findViewById(R.id.scan_lay);
		t_tv01 = (TextView) rl01.findViewById(R.id.common_title_tv01);
		t_iv01 = (ImageView) rl01.findViewById(R.id.common_title_iv01);
		t_iv01.setVisibility(View.VISIBLE);
		t_iv01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		spUtils = new SPUtils(Scan.this);

		GetInfo getInfo = new GetInfo();
		getInfo.execute("");

	}

	private class GridViewAdapter extends BaseAdapter {

		Context context;
		ArrayList<News> news_list;

		public GridViewAdapter(Context context, ArrayList<News> news_list) {
			this.context = context;
			this.news_list = news_list;
		}

		@Override
		public int getCount() {

			return news_list.size();
		}

		@Override
		public Object getItem(int position) {

			return news_list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.item, null);
			g_tv01 = (TextView) convertView.findViewById(R.id.item_tv01);
			View g_v02 = (View) convertView.findViewById(R.id.item_v02);
			if (position % 2 != 0) {
				g_v02.setVisibility(View.VISIBLE);
			}
			RelativeLayout rl01 = (RelativeLayout) convertView
					.findViewById(R.id.item_rl01);
			if (spUtils.getNight()) {
				g_tv01.setTextColor(Color.WHITE);
				ll01.setBackgroundColor(R.color.night);
			} else {
				g_tv01.setTextColor(Color.BLACK);
				ll01.setBackgroundColor(Color.WHITE);
			}
			g_tv01.setText((position + 1) + "."
					+ news_list.get(position).getTitle());
			return convertView;
		}
	}

	class GetInfo extends AsyncTask<String, Integer, Integer> {
		protected Integer doInBackground(String... params) {
			int status = 0;
			news_list = JsonUtils.GetNewsFromJson(Scan.this, type);
			if (news_list != null) {
				status = 1;
			} else {
				if (CommonUtil.isNetworkAvailable(Scan.this)) {
					CommonUtil.delFileByType(type);
					news_list = JsonUtils.GetNewsFromJson(Scan.this, type);
					status = 1;
				} else {
					status = -1;
				}
			}
			return status;

		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		protected void onPostExecute(Integer integer) {
			super.onPostExecute(integer);
			try {
				switch (integer) {
				case 1:
					if (news_list == null || news_list.size() == 0) {
						tv01.setText("当前无内容更新.");
						tv01.setVisibility(View.VISIBLE);
					} else {
						// paper = news_list.get(0).getPaper();
						t_tv01.setText(paper);
						// news_list.remove(news_list.get(0));
						tv01.setVisibility(View.GONE);
						pb01.setVisibility(View.GONE);
						gridViewAdapter = new GridViewAdapter(Scan.this,
								news_list);
						gv01.setAdapter(gridViewAdapter);
					}

					gv01.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								final int position, long arg3) {
							Intent intent = new Intent(Scan.this,
							    FlipComplexLayoutActivity.class);
//							Intent intent = new Intent(Scan.this,
//							    ViewNews.class);
							intent.putExtra("title", news_list.get(position)
									.getTitle());
							intent.putExtra("content", news_list.get(position)
									.getContent());
							intent.putExtra("paper", paper);
							intent.putExtra("current", position);

							ArrayList list = new ArrayList();
							list.add(news_list);
							intent.putParcelableArrayListExtra("news_list",
									list);
							startActivity(intent);
						}
					});
					break;
				case -1:
					tv01.setText("网络异常,请检查您的网络.");
					tv01.setVisibility(View.VISIBLE);

					break;

				default:
					break;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (g_tv01 != null) {
			if (spUtils.getNight()) {
				g_tv01.setTextColor(Color.WHITE);
				ll01.setBackgroundColor(R.color.night);
				gridViewAdapter.notifyDataSetChanged();
			} else {
				g_tv01.setTextColor(Color.BLACK);
				ll01.setBackgroundColor(Color.WHITE);
				gridViewAdapter.notifyDataSetChanged();
			}
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}