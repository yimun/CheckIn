package com.checkin.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.checkin.R;

/**
 * ����Ui�Ĺ㲥������
 * @author Administrator
 *
 */
public class UpdateUIReceiver extends BroadcastReceiver{

	TextView tv;
	
	public UpdateUIReceiver(TextView tv){
		this.tv = tv;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		int tempstate = intent.getIntExtra("state", 0 );
		switch(tempstate){
		case 0:
			tv.setText("��ע���ϰ�״̬");
			tv.setTextColor(R.color.green);
			break;
		case 1:
			tv.setText("δע���ϰ�״̬");
			tv.setTextColor(R.color.red);
			break;
		}
	}
	

}
