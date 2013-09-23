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
		//return sp.getString("ip_set", "NULL");
		return "192.168.9.124";
	}


	public int getType() {
		return sp.getInt("type_set", 1);
	}

	public String getSSID() {
		return sp.getString("ssid_set", "NULL");
	}

	public String getWifiPassword() {
		return sp.getString("password_set", "NULL");
	}
	
	public boolean getBootStart(){
		
		String get = sp.getString("is_boot_start", "true");
		return Boolean.parseBoolean(get);
	}
	
	public String getUnm(){
		return sp_user.getString("username", "NULL");
	}
	
	public String getPwd(){
		return sp_user.getString("password", "NULL");
	}
	
	public String getWcd(){
		return sp_user.getString("workcode", "NULL");
	}
	
	

}
