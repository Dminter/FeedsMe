package com.zncm.feedsme.ui;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zncm.feedsme.utils.CommonUtil;
import com.zncm.feedsme.utils.Constants;
import com.zncm.feedsme.utils.FeedsUtils;
import com.zncm.feedsme.utils.JsonUtils;
import com.zncm.feedsme.utils.NotificationUtils;
import com.zncm.feedsme.utils.SPUtils;
import com.zncm.feedsme.view.SlidingMenuView;

public class LeftMenu extends ActivityGroup implements OnClickListener {
	SPUtils spUtils;
	SlidingMenuView slidingMenuView;
	ImageView iv01, iv02, iv03, iv04 = null;
	TextView tv01, tv02, tv03, tv04, tv05, tv06 = null;
	ViewGroup tabcontent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.left_menu);

		spUtils = new SPUtils(LeftMenu.this);
		slidingMenuView = (SlidingMenuView) findViewById(R.id.sliding_menu_view);
		iv02 = (ImageView) findViewById(R.id.left_menu_iv02);
		iv02.setOnClickListener(this);
		iv03 = (ImageView) findViewById(R.id.left_menu_iv03);
		iv03.setOnClickListener(this);
		iv04 = (ImageView) findViewById(R.id.left_menu_iv04);
		iv04.setOnClickListener(this);

		tv01 = (TextView) findViewById(R.id.left_menu_tv01);
		tv02 = (TextView) findViewById(R.id.left_menu_tv02);
		tv03 = (TextView) findViewById(R.id.left_menu_tv03);
		tv04 = (TextView) findViewById(R.id.left_menu_tv04);
		tv05 = (TextView) findViewById(R.id.left_menu_tv05);
		tv06 = (TextView) findViewById(R.id.left_menu_tv06);
		tv01.setOnClickListener(this);
		tv02.setOnClickListener(this);
		tv03.setOnClickListener(this);
		tv04.setOnClickListener(this);
		tv05.setOnClickListener(this);
		tv06.setOnClickListener(this);
		tabcontent = (ViewGroup) slidingMenuView
				.findViewById(R.id.sliding_body);
		showDefaultTab(Constants.Feeds_like);
	}

	public void hideMenu(View view) {
		slidingMenuView.snapToScreen(1);
	}

	public void showLeftMenu(View view) {
		slidingMenuView.snapToScreen(0);
	}

	public void showRightMenu(View view) {
		slidingMenuView.snapToScreen(2);
	}

	void showDefaultTab(int rss_type) {
		getLocalActivityManager().removeAllActivities();
		tabcontent.removeAllViews();
		Intent intent = new Intent(this, Grid.class);
		intent.putExtra("rss_type", rss_type);
		View v = getLocalActivityManager().startActivity(Grid.class.getName(),
				intent).getDecorView();
		tabcontent.addView(v);
	}

	// public void changeTab1(){
	// Intent i = new Intent(this,Grid.class);
	// View v = getLocalActivityManager().startActivity(Grid.class.getName(),
	// i).getDecorView();
	// tabcontent.removeAllViews();
	// tabcontent.addView(v);
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_menu_iv02:
			DownLoadNews();
			break;
		case R.id.left_menu_iv03:
			DeleteNewsData();
			break;
		case R.id.left_menu_iv04:
			Intent intent = new Intent(LeftMenu.this, About.class);
			startActivity(intent);
			break;
		case R.id.left_menu_tv01:
			showDefaultTab(Constants.Feeds_like);
			break;

		case R.id.left_menu_tv02:
			showDefaultTab(Constants.Feeds_news);
			break;

		case R.id.left_menu_tv03:
			showDefaultTab(Constants.Feeds_it);
			break;

		case R.id.left_menu_tv04:
			showDefaultTab(Constants.Feeds_fun);
			break;

		case R.id.left_menu_tv05:
			showDefaultTab(Constants.Feeds_programme);
			break;
		case R.id.left_menu_tv06:
			showDefaultTab(Constants.Feeds_sports);
			break;

		default:
			break;
		}
	}

	private void DeleteNewsData() {
		CommonUtil.delAllFile(Constants.download_path);
		Toast.makeText(LeftMenu.this, "缓存已清理完毕.", Toast.LENGTH_SHORT).show();
	}

	private void DownLoadNews() {

		GetInfo getInfo = new GetInfo();
		getInfo.execute("");
		// String[] feeds_items_text = spUtils.getVery_like().substring(1)
		// .split("\\|");
		// ArrayList<String> feeds_items_key = new ArrayList<String>();
		//
		// for (int i = 0; i < feeds_items_text.length; i++) {
		// feeds_items_key.add(feeds_items_text[i].substring(
		// feeds_items_text[i].indexOf("#") + 1,
		// feeds_items_text[i].length()));
		// }
		//
		// boolean is_save_ok = false;
		// for (int i = 0; i < feeds_items_key.size(); i++) {
		// is_save_ok = JsonUtils.SaveJsonToSDCard(feeds_items_key.get(i),
		// FeedsUtils.SaveNews(feeds_items_key.get(i)));
		//
		// }
		//
		// if (is_save_ok) {
		// Toast.makeText(LeftMenu.this, "下载失败.", Toast.LENGTH_SHORT).show();
		// } else {
		// Toast.makeText(LeftMenu.this, "下载完成.", Toast.LENGTH_LONG).show();
		// }

	}

	class GetInfo extends AsyncTask<String, Integer, Integer> {
		private NumberFormat numberFormat = NumberFormat.getInstance();
		int totalTask = 0;
		int currentTask = 0;
		int percentTask = 0;
		ArrayList<String> feeds_items_key;
		ArrayList<String> feeds_items_name;

		protected Integer doInBackground(String... params) {

			int status = 0;
			boolean is_save_ok = false;

			String[] feeds_items_text = spUtils.getVery_like().substring(1)
					.split("\\|");
			feeds_items_key = new ArrayList<String>();
			feeds_items_name = new ArrayList<String>();
			for (int i = 0; i < feeds_items_text.length; i++) {
				feeds_items_key.add(feeds_items_text[i].substring(
						feeds_items_text[i].indexOf("#") + 1,
						feeds_items_text[i].length()));
				feeds_items_name.add(feeds_items_text[i].substring(0,
						feeds_items_text[i].indexOf("#")));
			}
			totalTask = feeds_items_key.size();
			if (CommonUtil.isNetworkAvailable(LeftMenu.this)) {
				NotificationUtils.DownProgressBar(LeftMenu.this, "开始下载..",
						Constants.n_pb_id);
				for (int i = 0; i < feeds_items_key.size(); i++) {
					if (CommonUtil.isNetworkAvailable(LeftMenu.this)) {
						publishProgress(i);
						is_save_ok = JsonUtils.SaveJsonToSDCard(
								feeds_items_key.get(i),
								FeedsUtils.SaveNews(feeds_items_key.get(i)));
					} else {
						status = -2;
					}
				}

				if (currentTask == totalTask) {
					status = 1;
				}
			} else {
				status = -1;

			}

			return status;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			currentTask = values[0] + 1;
			percentTask = Integer.parseInt(numberFormat.format(currentTask
					* 100 / totalTask));
			NotificationUtils.notification.contentView.setProgressBar(
					R.id.down_pb_pb01, 100, percentTask, false);
			NotificationUtils.notification.contentView.setTextViewText(
					R.id.down_pb_tv01, feeds_items_name.get(currentTask - 1)
							+ " " + percentTask + "%");
			NotificationUtils.manager.notify(Constants.n_pb_id,
					NotificationUtils.notification);
			if (percentTask == 100) {
				NotificationUtils.manager.cancel(Constants.n_pb_id);
			}

		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		protected void onPostExecute(Integer integer) {
			super.onPostExecute(integer);
			try {

				switch (integer) {
				case -2:
					Toast.makeText(LeftMenu.this, "网络连接失败,下载中断.",
							Toast.LENGTH_LONG).show();
					NotificationUtils.manager.cancel(Constants.n_pb_id);
				case -1:
					Toast.makeText(LeftMenu.this, "网络连接失败.", Toast.LENGTH_LONG)
							.show();
					break;
				case 1:
					Toast.makeText(LeftMenu.this, "下载完毕,即刻开启0流量阅读.",
							Toast.LENGTH_LONG).show();
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
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("onDestroyonDestroy11111111");
	}
   
}
