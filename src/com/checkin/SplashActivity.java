package com.checkin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.checkin.utils.PreferGeter;

/**
 * 启动界面，进行引导，
 * 未注册登录的引导入注册界面，已登录的进入主界面
 * @author Administrator
 *
 */

public class SplashActivity extends Activity{
	
	private final int SPLASH_DISPLAY_LENGHT = 1000; // 延迟2秒跳转
	protected Intent intent =new Intent();
	PreferGeter geter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		geter = new PreferGeter(this);
		
		// 判断是否已登陆，否则转到登录注册界面
		if (geter.getUnm().equalsIgnoreCase("NULL")) {		
			intent.setClass(getApplicationContext(), LoginActivity.class);
		}
		else{
			intent.setClass(getApplicationContext(), MainActivity.class);
		}
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				SplashActivity.this.finish();
				SplashActivity.this.startActivity(intent);
				
			}

		}, SPLASH_DISPLAY_LENGHT);
		
	}

}
