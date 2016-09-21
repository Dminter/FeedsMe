package com.zncm.feedsme.ui;

import java.io.File;

import com.zncm.feedsme.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		File fileDir = new File(Constants.download_path);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		Intent intent = new Intent(Main.this, LeftMenu.class);
		startActivity(intent);
		finish();
	}

}