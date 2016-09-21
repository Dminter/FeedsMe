package com.zncm.feedsme.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.zncm.feedsme.ui.LeftMenu;
import com.zncm.feedsme.ui.R;

public class NotificationUtils {
	public static Notification notification = null;
	public static NotificationManager manager = null;

	public static void DownProgressBar(Context context, String down_news,
			int n_pb_id) {
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.n_icon, down_news + "开始下载",
				System.currentTimeMillis());
		Intent intent = new Intent(context, LeftMenu.class);
		PendingIntent pendIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, down_news + "下载完成,点击查看",
				"下载完成 点击安装", pendIntent);
		notification.contentView = new RemoteViews(context.getPackageName(),
				R.layout.down_pb);

		notification.contentView.setProgressBar(R.id.down_pb_pb01, 100, 0,
				false);
		notification.contentView.setTextViewText(R.id.down_pb_tv01, down_news
				+ " " + 0 + "%");

		try {
			manager.notify(n_pb_id, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}