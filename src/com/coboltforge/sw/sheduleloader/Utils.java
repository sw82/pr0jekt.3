package com.coboltforge.sw.sheduleloader;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

	private static SharedPreferences settings;
	private static SharedPreferences.Editor editor;


	public static void saveUserSettings(Context c, String type, String value) {
		settings = c.getSharedPreferences(Constants.PREFSNAME, 0);
		editor = settings.edit();
		editor.putString(type, value);
		editor.commit();				
	}

	public static String getUserSettings(Context c, String type) {

		settings = c.getSharedPreferences(Constants.PREFSNAME, 0);
		return(settings.getString(type, null));
	}
}
