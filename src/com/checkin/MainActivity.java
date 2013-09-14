package com.checkin;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.checkin.service.MyService;
import com.checkin.service.UpdateUIReceiver;

public class MainActivity extends Activity {
	
	TextView tv;
	UpdateUIReceiver updateuiRec;
	final String UPDATE_ACTION = "com.checkin.updateui";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent s=new Intent(this,MyService.class);   
        this.startService(s); 
        tv = (TextView)findViewById(R.id.showstate);
        updateuiRec = new UpdateUIReceiver(tv);
        IntentFilter inf = new IntentFilter();
        inf.addAction(UPDATE_ACTION);
        this.registerReceiver(updateuiRec, inf);
	}
	
	@Override
	public void onDestroy(){
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
		case R.id.item1:
			
			break;
		case R.id.item2:
			Intent in = new Intent(this,LoginActivity.class);
			startActivity(in);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
		
	}
	

}
