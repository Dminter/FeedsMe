package com.zncm.feedsme.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;

import com.zncm.feedsme.pojo.News;

public class JsonUtils {

	public static String toJson(Object obj) {
		Writer writer = new StringWriter();
		String result = "";
		try {
			ObjectMapper om = new ObjectMapper();
			om.writeValue(writer, obj);
			result = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
					writer = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	private static String GetJsonFromSDCard(String type) {
		String jsonContent = "";
		try {

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				final String PATH = "/mnt/sdcard/FeedsMe/download/";
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
				String now = format.format(date);
				final String FILENAME = "FeedsMe#" + type + "#" + now + ".txt";
				File file = new File(PATH + FILENAME);
				if (file.exists()) {
					FileInputStream inputStream = new FileInputStream(file);
					long len = file.length();
					byte[] bytes = new byte[(int) len];
					inputStream.read(bytes);
					inputStream.close();
					jsonContent = new String(bytes);

				} else {
					jsonContent = null;
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return jsonContent;

	}

	public static ArrayList<News> GetNewsFromJson(Context context, String type) {
		String jsonString = "";
		try {
			jsonString = GetJsonFromSDCard(type);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonString == null) {
			return FeedsUtils.GetNews(type);
		}

		ArrayList<News> news_list = new ArrayList<News>();

		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			if (jsonArray.length() <= 0) {
				return null;
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.opt(i);
				News news = new News();
				news.setPaper(json.getString("paper"));
				news.setTitle(json.getString("title"));
				news.setContent(json.getString("content"));
				news_list.add(news);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return news_list;

	}

	public static boolean SaveJsonToSDCard(String fileName, String jsonContent) {

		boolean is_saved = false;
		try {

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				final String PATH = "/mnt/sdcard/FeedsMe/download/";
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
				String now = format.format(date);
				final String FILENAME = "FeedsMe#" + fileName + "#" + now
						+ ".txt";
				File fileDir = new File(PATH);
				File file = new File(PATH + FILENAME);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream outputStream = new FileOutputStream(file);
				outputStream.write(jsonContent.getBytes());
				outputStream.close();

				is_saved = true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return is_saved;
	}

}