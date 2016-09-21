package com.zncm.feedsme.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zncm.feedsme.ui.R;

/**
 * 
 * @author Dminter
 * @2012-12-10 下午1:04:35
 * @TODO 工具类
 */
public class CommonUtil {

	private static final String DAY = "天";
	private static final String HOUR = "小时";
	private static final String MINUTE = "分钟";
	private static final String UNKNOWN = "";
	private static final String SUFFIX = "前";

	public static String getDisplayDate(Date inputDate) {
		Date localDate = new Date();
		long l1 = (localDate.getTime() - inputDate.getTime()) / 1000L;
		while (true) {
			if (l1 <= 0L) {
				return UNKNOWN;
			}
			long l2 = l1 / 60L;
			if (l2 < 60L) {
				return l2 + MINUTE + SUFFIX;
			}

			long l3 = l2 / 60L;
			if (l3 < 24L) {
				return l3 + HOUR + SUFFIX;
			}
			long l4 = l3 / 24L;
			return l4 + DAY + SUFFIX;

		}

	}

	public static boolean exitPopupWindow(Context context,
			final Activity activity) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.exit_bar, null);
		TextView tv01 = (TextView) view.findViewById(R.id.exit_bar_tv01);
		TextView tv02 = (TextView) view.findViewById(R.id.exit_bar_tv02);

		final PopupWindow popupWindow = new PopupWindow(view,
				CommonUtil.getWH(activity)[0] - 40, 150);
		popupWindow.showAsDropDown(view, 20,
				CommonUtil.getWH(activity)[1] - 150);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popupWindow.showAtLocation(null, Gravity.CENTER_VERTICAL, 0, 0);

		tv01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("1111111111");
			}
		});
		tv02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				activity.finish();
			}
		});

		return true;

	}

	public static int[] getWH(Activity context) {

		int wh[] = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		wh[0] = dm.widthPixels;
		wh[1] = dm.heightPixels;
		return wh;

	}

	public static void delFileByType(String type) {
		File file = new File(Constants.download_path);
		String[] tempList = file.list();
		File temp = null;

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		String now = format.format(date);
		String FILENAME = "FeedsMe#" + type + "#" + now + ".txt";
		if (tempList.length > 0) {
			for (int i = 0; i < tempList.length; i++) {
				if (tempList[i].equals(FILENAME)) {
					temp = new File(Constants.download_path + tempList[i]);
					if (temp.isFile()) {
						temp.delete();
					}
				}

			}
		}

	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String stripHtml(String content) {
		// <p>段落替换为换行
		content = content.replaceAll("<p .*?>", "\r\n");
		// <br><br/>替换为换行
		content = content.replaceAll("<br\\s*/?>", "\r\n");
		// 去掉其它的<>之间的东西
		content = content.replaceAll("\\<.*?>", "");

		return content;
	}

	public static void copyText(Context context, String text) {
		ClipboardManager cm = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cm.setText(text);
		ShowText(context, "内容已经复制到剪切板.");
	}

	public static void sendSMS(Context context, String content) {
		Uri uri = Uri.parse("smsto:" + "");
		Intent intent1 = new Intent(Intent.ACTION_SENDTO, uri);
		if (content.length() <= 65) {
			intent1.putExtra("sms_body", content);
		} else {
			intent1.putExtra("sms_body", content.substring(0, 65) + "...");
		}

		context.startActivity(intent1);
	}

	/**
	 * 
	 * @author Dminter
	 * @2012-12-20 上午10:58:59
	 * @TODO 显示小提示
	 * @param text
	 */
	public static void ShowText(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 替换回车空格
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 限制输入字符控制在120字以内
	 * 
	 * @param str
	 * @return
	 */
	public static String limit120Words(String str) {
		String dest = "";
		if (str != null) {
			if (str.length() <= 120) {
				dest = str;
			} else {
				dest = str.substring(0, 120);
			}
		}
		return dest;
	}

	/**
	 * 得到手机串号
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		String deviceId = "";
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = tm.getDeviceId();
		if (deviceId != null) {
			return deviceId;
		} else {
			return null;
		}

	}

	/**
	 * 检测网络状况
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}

	}

	// WIFI是否开启
	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
