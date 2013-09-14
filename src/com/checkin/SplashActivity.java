package com.checkin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;



public class SplashActivity extends Activity{
	
	private final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟2秒跳转
	protected Intent intent =new Intent();
	SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		preferences = getSharedPreferences("IsLogin", MODE_PRIVATE);
		int id = preferences.getInt("ID", 0);
		
		// 判断是否已注册登陆，否则转到登陆界面
		if (id == 0) {		
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
