package com.coboltforge.sw.sheduleloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;


public class SpicariBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = Constants.TAG;

    @Override 
    public void onReceive(Context context, Intent intent) {
	Log.d(TAG, "Broadcast received");
      Toast.makeText(context, "Don't panic but your time is up!!!!.",
          Toast.LENGTH_LONG).show();
      // Vibrate the mobile phone
      Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
      vibrator.vibrate(2000);
    }
}