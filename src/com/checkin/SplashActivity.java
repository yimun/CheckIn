package com.checkin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.checkin.utils.PreferGeter;



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
		
		// 判断是否已登陆，否则转到设置登录界面
		if (geter.getUsername().equalsIgnoreCase("NULL")) {		
			intent.setClass(getApplicationContext(), LoginActivity.class);
		}
		else{
			intent.setClass(getApplicationContext(), MainActivity.class);
		}
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);
		
	}

}
