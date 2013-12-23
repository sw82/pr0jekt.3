package com.coboltforge.sw.fragments;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.coboltforge.sw.sheduleloader.AlarmReceiverActivity;
import com.coboltforge.sw.sheduleloader.Constants;
import com.coboltforge.sw.sheduleloader.Utils;
import com.coboltforge.sw.spicari.R;
/**
 *TODO BOOT_COMPLETED receiver 
 *http://michael.theirwinfamily.net/articles/android/android-creating-alarm-alarmmanager
 */
public  class  NotificationSectionFragment extends Fragment {
    Context mContex;
    private PendingIntent pendingIntent;
    public NotificationSectionFragment() {
    }

    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = Constants.TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	//get context
	mContex = inflater.getContext();
	View root = inflater.inflate(R.layout.notification, container, false);

	//get controls
	final CheckBox cb_auto = (CheckBox) root.findViewById(R.id.checkBox_auto);
	final TimePicker tp = (TimePicker) root.findViewById(R.id.timePicker);
	final Button button_apply = (Button) root.findViewById(R.id.button_apply);

	//set initial values,  if present
	tp.setIs24HourView(true);

	if(Utils.getUserSettings(mContex, Constants.SettingsHour) !=null)
	    tp.setCurrentHour(Integer.parseInt(Utils.getUserSettings(mContex, Constants.SettingsHour)));
	if(Utils.getUserSettings(mContex, Constants.SettingsMinute) !=null)
	    tp.setCurrentMinute(Integer.parseInt(Utils.getUserSettings(mContex, Constants.SettingsMinute)));

	Log.d(TAG, "Setting values:\n " 
		+ "\t[hour] \t" + Utils.getUserSettings(mContex, Constants.SettingsHour)  +"\n"
		+ "\t[minute] \t" + Utils.getUserSettings(mContex, Constants.SettingsMinute) +"\n"
		+ "\t[automode]\t" + cb_auto.isChecked() +"\n" );


	button_apply.setOnClickListener(new Button.OnClickListener(){

	    @Override
	    public void onClick(View arg0) {

		//		Intent myIntent = new Intent(mContex, MyAlarmService.class);
		//		pendingIntent = PendingIntent.getService(mContex, 0, myIntent, 0);
		//		AlarmManager alarmManager = (AlarmManager) mContex.getSystemService(Context.ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(System.currentTimeMillis());

		calendar.add(Calendar.SECOND, 10);

		//		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

		Toast.makeText(mContex, "Start Alarm", Toast.LENGTH_LONG).show();

		//save settings
		Utils.saveUserSettings(mContex, Constants.SettingsHour,  String.valueOf(tp.getCurrentHour()));
		Utils.saveUserSettings(mContex, Constants.SettingsMinute, String.valueOf(tp.getCurrentMinute()));

		Log.d(TAG, "Saving values: \n"
			+ "\t[hour] \t" + tp.getCurrentHour()  +"\n"
			+ "\t[minute] \t" + tp.getCurrentMinute() +"\n"
			+ "\t[automode]\t" + cb_auto.isChecked() +"\n" );

		
		
		Calendar AlarmCal = Calendar.getInstance();
		AlarmCal.setTimeInMillis(System.currentTimeMillis());
		AlarmCal.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());  // set user selection
		AlarmCal.set(Calendar.MINUTE, tp.getCurrentMinute());        // set user selection
		AlarmCal.set(Calendar.SECOND, 0);

		
		//Create a new PendingIntent and add it to the AlarmManager
	        Intent intent = new Intent(mContex, AlarmReceiverActivity.class);
	        PendingIntent pendingIntent = PendingIntent.getActivity(mContex,
	            12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	        AlarmManager am = 
	            (AlarmManager) mContex.getSystemService(mContex.ALARM_SERVICE);
	        am.set(AlarmManager.RTC_WAKEUP, AlarmCal.getTimeInMillis(),
	                pendingIntent);
		
		
//		
//		Intent alarmIntent = new Intent(mContex, AlarmReceiverActivity.class);
//		alarmIntent.putExtra("nel.example.alarms1","My message");
//		
//		pendingIntent = PendingIntent.getService(mContex, 0,
//			alarmIntent, 0);
//		
//		AlarmManager alarmManager = (AlarmManager) mContex.getSystemService(mContex.ALARM_SERVICE);
//
////		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
////			AlarmCal.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES,
////			pendingIntent);
//
//		alarmManager.set(AlarmManager.RTC_WAKEUP, AlarmCal.getTimeInMillis(),
//			pendingIntent);


	    }});





	//	Intent intent = new Intent(mContex,  SpicariBroadcastReceiver.class);
	//	PendingIntent pendingIntent = PendingIntent.getBroadcast(mContex, 234324243, intent, 0);
	//	AlarmManager alarmManager = (AlarmManager) mContex.getSystemService(mContex.ALARM_SERVICE);
	//	alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
	//		+ (10 * 1000), pendingIntent);
	//	Toast.makeText(mContex, "Alarm set in " + 10 + " seconds",
	//		Toast.LENGTH_LONG).show();  

	return root;
    }
}


