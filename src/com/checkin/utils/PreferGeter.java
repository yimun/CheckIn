package com.checkin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferGeter {

	private Context context;
	private SharedPreferences sp;
	private SharedPreferences sp_user;

	public PreferGeter(Context con) {

		this.context = con;
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp_user = context.getSharedPreferences("user", Context.MODE_PRIVATE);

	}

	// À´×ÔSettingActivity µÄ Preference
	public String getIP() {
		return sp.getString("ip_set", null);
	}

	public String getPort() {

		return sp.getString("port_set", null);

	}

	public int getType() {
		return sp.getInt("ssid_set", 1);
	}

	public String getSSID() {
		return sp.getString("ssid_set", null);
	}

	public String getWifiPassword() {
		return sp.getString("password_set", null);
	}
	
	public String getUsername(){
		
		return sp_user.getString("username", " ");
		
	}

}
