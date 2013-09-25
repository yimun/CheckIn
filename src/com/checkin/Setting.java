package com.checkin;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
/**
 * 设置界面（暂时弃用）
 * @author Administrator
 *
 */
public class Setting extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 所的的值将会自动保存到SharePreferences
		addPreferencesFromResource(R.xml.setting);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		return false;
	}

}
