package com.checkin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import com.checkin.MainActivity;
import com.checkin.R;
import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;
import com.checkin.utils.WifiAdmin;
import com.checkin.utils.WifiAdmin.WifiCipherType;

public class MyService extends Service {

	static int intCounter = 0;
	static boolean runFlag = true;
	static final int DELAY = 10000; // 刷新频率
	final String UPDATE_ACTION = "com.checkin.updateui";// 更新前台UI
	// String str_compSSID = "Connectify-me"; // 公司的wifi名
	// String str_compKEY = "dianxin1212"; // 公司的wifi密码 ，无密码则为null
	String tag = "wifi service";

	// 定义网络检测信号返回值，上下班和无响应信号

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {

		super.onCreate();
		Log.i(tag, "启动服务");
		new ScanTask(this).start();

	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(tag, "onBind()");
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		// objHandler.removeCallbacks(mTasks);
		super.onDestroy();
	}

	public class ScanTask extends Thread {

		private Context context;
		private WifiAdmin mwifiAdmin;
		private SocketUtil connect;
		private PreferGeter geter;

		public ScanTask(Context con) {
			this.context = con;
			geter = new PreferGeter(con);
		}

		public void run() {
			while (runFlag) {

				intCounter++;
				Log.i("CheckIn",
						"Service Counter:" + Integer.toString(intCounter));
				mwifiAdmin = new WifiAdmin(this.context);
				if (mwifiAdmin == null)
					Log.i(tag, "mwifiAdmin is null");
				if (isSendSignal()) { // 到达信号，上班
					connect = new SocketUtil(this.context);
					if (!connect.isConnected) {
						try {
							connect.connectServer();
						} catch (Exception e) {
							Toast.makeText(this.context, "连接服务器失败，请检查网络状况..",
									Toast.LENGTH_LONG).show();
							return;
						}
					}
					boolean isCheck = connect.sendCheck();
					connect.close();

				} else {
					// 无响应信号，及原有状态将保持
					break;
				}
				try {
					Log.i(tag, "Thread sleep");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.i(tag, "InterruptedException runflag = false");
					MyService.runFlag = false;
				}
			}

		}

		private boolean isSendSignal() {
			WifiCipherType TYPE;
			if (geter.getType() == 1) {
				TYPE = WifiCipherType.WIFICIPHER_WPA;
			} else if (geter.getType() == 2) {
				TYPE = WifiCipherType.WIFICIPHER_WEP;
			} else {
				TYPE = WifiCipherType.WIFICIPHER_NOPASS;
			}
			String SSID = geter.getSSID();
			String password = geter.getWifiPassword();

			mwifiAdmin.openWifi();
			while (true) {
				if (mwifiAdmin.getSSID().equals(SSID)) {
					return true;
				} else {
					if (mwifiAdmin.isCanScan(SSID)) { // 判断是否能够扫描得到
						Log.i(tag, "能够扫描到");
						// 若当前已连接至某个wifi，先断开
						if (mwifiAdmin.getSSID() != null) {
							mwifiAdmin
									.disconnectWifi(mwifiAdmin.getNetworkId());
						}
						mwifiAdmin.Connect(SSID, password, TYPE);
						try {
							Thread.currentThread();
							Thread.sleep(15000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						continue;

					} else {
						
						Log.i(tag, "无法扫描到");
						return false;
					}
				}
			}
		}

	}

	/*public void showNotif(Context context) {
		// TODO Auto-generated method stub

		String title;
		String content;

		// 消息通知栏
		// 定义NotificationManager
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		// 定义通知栏展现的内容信息

		long when = System.currentTimeMillis();

		title = "已注册成功";
		content = "您已成功注册到公司";

		Notification notification = new Notification.Builder(context)
				.setContentTitle(title).setContentText(content)
				.setSmallIcon(R.drawable.ic_launcher).build();

		// 定义下拉通知栏时要展现的内容信息
		CharSequence contentTitle = "我的通知栏标展开标题";
		CharSequence contentText = "我的通知栏展开详细内容";
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
		mNotificationManager.notify(1, notification);

	}*/
}