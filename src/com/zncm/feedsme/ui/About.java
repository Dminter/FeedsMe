package com.zncm.feedsme.ui;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.UMFeedbackService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class About extends Activity {
	TextView t_tv01 = null;
	ImageView t_iv01;
	TextView tv01 = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		RelativeLayout rl01 = (RelativeLayout) findViewById(R.id.about_lay);
		t_tv01 = (TextView) rl01.findViewById(R.id.common_title_tv01);
		t_tv01.setText("关于我们");
		t_iv01 = (ImageView) rl01.findViewById(R.id.common_title_iv01);
		t_iv01.setVisibility(View.VISIBLE);
		t_iv01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tv01 = (TextView) findViewById(R.id.about_feedback);
		tv01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UMFeedbackService.openUmengFeedbackSDK(About.this);
			}
		});
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