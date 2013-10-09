package com.checkin.service;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class WakeLockManger {
	
	private Context mContext;
	// 用WakeLock
	private WakeLock mWakeLock;

	// 服务启动时acquire，退出时release

	public WakeLockManger(Context mContext) {
		this.mContext = mContext;
	}

	// 申请设备电源锁
	public void acquireWakeLock() {
		if (null == mWakeLock) {
			PowerManager pm = (PowerManager) mContext
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "com.checkin");
			if (null != mWakeLock) {
				mWakeLock.acquire();
			}
		}
	}

	// 释放设备电源锁
	public void releaseWakeLock() {
		if (null != mWakeLock) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

}
