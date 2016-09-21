package com.zncm.feedsme.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zncm.feedsme.utils.Constants;
import com.zncm.feedsme.utils.SPUtils;

public class Grid extends Activity {
	long firstTime = 0;
	SPUtils spUtils;
	LinearLayout ll01 = null;
	String[] feeds_items_text = null;
	GridView gridView = null;
	GridAdapter gridAdapter = null;

	TextView t_tv01 = null;

	boolean is_like = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		MobclickAgent.onError(Grid.this);
		spUtils = new SPUtils(Grid.this);
		if (!spUtils.getIs_update()) {
			// Umeng的自动更新
			UmengUpdateAgent.update(Grid.this);
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			spUtils.setIs_update(true);
		}
		ll01 = (LinearLayout) findViewById(R.id.grid_ll01);
		RelativeLayout rl01 = (RelativeLayout) findViewById(R.id.grid_lay);
		t_tv01 = (TextView) rl01.findViewById(R.id.common_title_tv01);

		if (spUtils.getNight()) {
			ll01.setBackgroundColor(R.color.night);
		} else {
			ll01.setBackgroundColor(Color.WHITE);
		}

		int rss_type = getIntent().getExtras().getInt("rss_type");
		switch (rss_type) {
		case Constants.Feeds_like:
			t_tv01.setText("Ф喜欢Ψ");
			if (spUtils.getVery_like().length() > 0) {
				feeds_items_text = spUtils.getVery_like().substring(1)
						.split("\\|");
			} else {
				feeds_items_text = getResources().getStringArray(
						R.array.news_items);
			}

			break;
		case Constants.Feeds_news:
			t_tv01.setText("Ф新闻Ψ");
			feeds_items_text = getResources()
					.getStringArray(R.array.news_items);
			break;
		case Constants.Feeds_fun:
			t_tv01.setText("Ф有趣Ψ");
			feeds_items_text = getResources().getStringArray(R.array.fun_items);
			break;
		case Constants.Feeds_it:
			t_tv01.setText("Ф科技Ψ");
			feeds_items_text = getResources().getStringArray(R.array.it_items);
			break;
		case Constants.Feeds_programme:
			t_tv01.setText("Ф编程Ψ");
			feeds_items_text = getResources().getStringArray(
					R.array.programme_items);
			break;
		case Constants.Feeds_sports:
			t_tv01.setText("Ф体育Ψ");
			feeds_items_text = getResources().getStringArray(
					R.array.sports_items);
			break;

		default:
			break;
		}

		gridView = (GridView) findViewById(R.id.grid_gv01);
		gridAdapter = new GridAdapter(Grid.this);
		gridView.setAdapter(gridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = new Intent(Grid.this, Scan.class);
				intent.putExtra("type", feeds_items_text[position].substring(
						feeds_items_text[position].indexOf("#") + 1,
						feeds_items_text[position].length()));
				intent.putExtra("paper", feeds_items_text[position].substring(
						0, feeds_items_text[position].indexOf("#")));
				startActivity(intent);

			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (!spUtils.getVery_like()
						.contains(feeds_items_text[position])) {
					Dialog dialog = new AlertDialog.Builder(Grid.this)
							.setItems(R.array.is_like_items,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											spUtils.setVery_like(spUtils
													.getVery_like()
													+ "|"
													+ feeds_items_text[position]);

											gridAdapter.notifyDataSetChanged();
										}
									})

							.create();
					dialog.show();
				} else {
					Dialog dialog = new AlertDialog.Builder(Grid.this)
							.setItems(R.array.is_not_like_items,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											spUtils.setVery_like(spUtils
													.getVery_like()
													.replace(
															"|"
																	+ feeds_items_text[position],
															""));
											gridAdapter.notifyDataSetChanged();
										}
									})

							.create();
					dialog.show();
				}
				return false;
			}
		});
	}

	class GridAdapter extends BaseAdapter {
		Context context = null;

		public GridAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {

			return feeds_items_text.length;
		}

		@Override
		public Object getItem(int position) {

			return feeds_items_text[position];
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = new TextView(context);
			textView.setText(feeds_items_text[position].substring(0,
					feeds_items_text[position].indexOf("#")));
			textView.setWidth(72);
			textView.setHeight(72);
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(20);
			textView.setGravity(Gravity.CENTER);
			if (spUtils.getVery_like().contains(feeds_items_text[position])) {
				textView.setBackgroundResource(R.color.like);
			} else if (!spUtils.getVery_like().contains(
					feeds_items_text[position])) {
				textView.setBackgroundResource(R.color.not_like);
			}

			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
			String now = format.format(date);
			String FILENAME = "FeedsMe#"
					+ feeds_items_text[position].substring(
							feeds_items_text[position].indexOf("#") + 1,
							feeds_items_text[position].length()) + "#" + now
					+ ".txt";
			File fileDir = new File(Constants.download_path);

			boolean is_down = false;
			if (fileDir.listFiles().length > 0) {
				File[] temp = fileDir.listFiles();
				for (int i = 0; i < temp.length; i++) {
					if (temp[i].getName().equals(FILENAME)) {
						is_down = true;
					}
				}
			}
			if (is_down) {
				textView.setBackgroundResource(R.color.is_down);
			}

			return textView;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

		if (ll01 != null) {
			if (spUtils.getNight()) {
				ll01.setBackgroundColor(R.color.night);
			} else {
				ll01.setBackgroundColor(Color.WHITE);
			}
		}

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			long secondTime = System.currentTimeMillis();
//			if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
//				Toast.makeText(Grid.this, "再按一次退出程序...", Toast.LENGTH_SHORT)
//						.show();
//				firstTime = secondTime;// 更新firstTime
//				return true;
//			} else {
//				spUtils.setIs_update(false);
//				System.exit(0);// 否则退出程序
//			}
//		}
//		return super.onKeyUp(keyCode, event);
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("onDestroyonDestroy12222222222");
	}
	
	

}