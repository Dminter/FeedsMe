package com.zncm.feedsme.utils;

import java.util.HashMap;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

	Context context;
	SharedPreferences sp;

	public SPUtils(Context context) {
		this.context = context;
		this.sp = this.context.getSharedPreferences("sp_info", 0);
		// 0 MODE_PRIVATE 得到已经存好的sp
	}

	public boolean getIs_update() {
		return this.sp.getBoolean("is_update", false);
	}

	public void setIs_update(boolean is_update) {
		SharedPreferences.Editor editor = this.sp.edit();
		editor.putBoolean("is_update", is_update);
		editor.commit();
	}

	public String getVery_like() {
		return this.sp.getString("very_like", "");

	}

	public void setVery_like(String very_like) {
		SharedPreferences.Editor editor = this.sp.edit();
		editor.putString("very_like", very_like);
		editor.commit();
	}

	public int getFont_size() {
		return this.sp.getInt("font_size", 16);
	}

	public void setFont_size(int font_size) {
		SharedPreferences.Editor editor = this.sp.edit();
		editor.putInt("font_size", font_size);
		editor.commit();
	}

	public boolean getNight() {
		return this.sp.getBoolean("is_night", false);
	}

	public void setNight(boolean is_night) {
		SharedPreferences.Editor editor = this.sp.edit();
		editor.putBoolean("is_night", is_night);
		editor.commit();
	}

}
