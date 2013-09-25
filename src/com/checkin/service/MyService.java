package com.checkin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.checkin.MainActivity;
import com.checkin.R;
import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;
import com.checkin.utils.WifiAdmin.WifiCipherType;
/**
 * 后台发送签到信息主服务，
 * 判断是否能与服务器连接并发送消息
 * @author Administrator
 *
 */
public class MyService extends Service {

	static int intCounter;
	static boolean runFlag = true;
	static final int DELAY = 2 * 60 * 1000; // 刷新频率2分钟
	static int noSignCounter;
	final String UPDATE_ACTION = "com.checkin.updateui";// 更新前台UI
	String tag = "MyService";
	public static boolean isCheck = false;
	ScanTask task;
	Intent in;

	// 接受网络检测信号返回值并更新UI线程
	Handler hd = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Log.i(tag, "发送首次签到成功通知");
				showNotif(MyService.this, true);
				in = new Intent();
				in.setAction(UPDATE_ACTION);
				in.putExtra("state", 0);
				sendBroadcast(in);
				break;
			case 1:
				Log.i(tag, "发送离开通知通知");
				showNotif(MyService.this, false);
				in = new Intent();
				in.setAction(UPDATE_ACTION);
				in.putExtra("state", 1);
				sendBroadcast(in);
				break;

			}
		}
	};
	
	@Override
	public void onCreate() {

		super.onCreate();
		Log.i(tag, "onCreate()启动服务");
		task = new ScanTask(this);
		task.start();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub

		super.onStart(intent, startId);
		Log.i(tag, "onStart");
		noSignCounter = intCounter = 0;
		isCheck = false;
		runFlag = true;

	}



	@Override
	public IBinder onBind(Intent intent) {
		Log.i(tag, "onBind()");
		return null;
	}

	@Override
	public void onDestroy() {

		// TODO Auto-generated method stub
		Log.i(tag, "onDestroy");
		hd.removeCallbacks(task);
		super.onDestroy();

	}

	/**
	 * 后台扫描通信线程
	 * @author Administrator
	 *
	 */
	public class ScanTask extends Thread {

		private SocketUtil connect;
		private PreferGeter geter;
		private String ip;
		private String username, password, workcode;
		WifiCipherType TYPE;
		boolean get;

		public ScanTask(Context con) {
			geter = new PreferGeter(con);
			ip = geter.getIP();
			username = geter.getUnm();
			password = geter.getPwd();
			workcode = geter.getWcd();
		}

		public void run() {
			Looper.prepare();
			while (runFlag) {

				get = false;
				intCounter++;
				Log.i("CheckIn",
						"Service Counter:" + Integer.toString(intCounter));
				connect = new SocketUtil(ip);
				if (!connect.isConnected) {
					try {
						connect.connectServer();
						get = connect.sendCheck(username, password, workcode);
						connect.close();
					} catch (Exception e) {
						e.printStackTrace();
						Log.i(tag, "noSignCounter=" + noSignCounter);
						noSignCounter++; // 连接失败次数统计
					}
				}

				if (!isCheck && get) { // 首次签到
					isCheck = true;

					Message tempMessage = new Message();
					tempMessage.what = 0;
					MyService.this.hd.sendMessage(tempMessage);
				}
				if (isCheck && !get) { // 离开
					isCheck = false;
					Message tempMessage = new Message();
					tempMessage.what = 1;
					MyService.this.hd.sendMessage(tempMessage);
				}

				// 连续5次无响应，则结束服务
				if (noSignCounter >= 5) {

					Log.i(tag, "结束服务指令");
					runFlag = false;
					MyService.this.stopSelf();
				}

				try {
					Log.i(tag, "Thread sleep");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.i(tag, "InterruptedException runflag = false");
					// MyService.runFlag = false;
				}
			}

		}
	}

	/**
	 * 签到和离开的通知栏显示
	 * 
	 * @param context
	 * @param isArrive
	 *            是否为签到成功通知
	 */
	public void showNotif(Context context, boolean isArrive) {

		// 定义通知栏展现的内容信息
		CharSequence title, contentTitle, contentText;
		if (isArrive) {
			title = "注册成功";
			contentTitle = "注册成功";
			contentText = "您已成功注册到811";

		} else {
			title = "离开";
			contentTitle = "离开";
			contentText = "您已离开811";
		}

		// 消息通知栏
		// 定义NotificationManager
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;

		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, title, when);
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// 定义下拉通知栏时要展现的内容信息
		Intent in = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, in, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
		mNotificationManager.notify(1, notification);

	}

}