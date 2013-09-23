package com.checkin;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.checkin.service.MyService;
import com.checkin.service.UpdateUIReceiver;
import com.checkin.utils.PreferGeter;

public class MainActivity extends Activity {

	TextView tv;
	TextView user;
	// Button start_btn, stop_btn;
	UpdateUIReceiver updateuiRec;
	final String UPDATE_ACTION = "com.checkin.updateui";
	Intent s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		s = new Intent(this, MyService.class);
		tv = (TextView) findViewById(R.id.showstate);
		user = (TextView) findViewById(R.id.user);
	

		updateuiRec = new UpdateUIReceiver(tv);
		IntentFilter inf = new IntentFilter();
		inf.addAction(UPDATE_ACTION);
		this.registerReceiver(updateuiRec, inf);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(updateuiRec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		/*
		 * case R.id.item1:
		 * 
		 * startActivity(new Intent(this, Setting.class)); break;
		 */

		case R.id.item1:

			startActivity(new Intent(this, RegistActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	public void onResume() {
		super.onResume();
		if(!isWorked()){
			startService(new Intent(this,MyService.class));
		}
		user.setText(new PreferGeter(this).getUnm());
		if (MyService.isCheck) {
			tv.setText("ÒÑ×¢²áÉÏ°à×´Ì¬");
		} else {
			tv.setText("Î´×¢²áÉÏ°à×´Ì¬");
		}
	}

	/**
	 * ÅÐ¶Ï¸Ã·þÎñÊÇ·ñ´æÔÚ
	 * 
	 * @return
	 */
	public boolean isWorked() {
		ActivityManager myManager = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("com.checkin.service.MyService")) {
				return true;
			}
		}
		return false;
	}

}
