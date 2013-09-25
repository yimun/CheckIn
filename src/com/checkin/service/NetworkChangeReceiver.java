package com.checkin.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

/**
 * 网络状态改变时接收广播并启动主服务
 * @author Administrator
 *
 */
public class NetworkChangeReceiver extends BroadcastReceiver {


	public void onReceive(Context ctx, Intent intent) {

		if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 这个监听wifi的连接状态
			//Log.i("receiver","NETWORK_STATE_CHANGED_ACTION");
			Parcelable parcelableExtra = intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (null != parcelableExtra) {
				NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
				State state = networkInfo.getState();
				if (state == State.CONNECTED) { // 表示wifi状态改变
					Log.i("receiver", "wifi状态改变");
					// start service
					Intent s = new Intent(ctx, MyService.class);
					ctx.startService(s);
				}

			}
			
		}

	

	}

}
