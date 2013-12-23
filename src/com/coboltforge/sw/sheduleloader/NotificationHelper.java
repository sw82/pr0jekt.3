package com.coboltforge.sw.sheduleloader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


/**
 * Source {@link http://eliasbland.wordpress.com/2011/03/11/an-example-of-how-to-run-a-background-task-and-report-progress-in-the-status-bar-using-asynctask-on-android/}
 * @author basti
 */
public class NotificationHelper {
    private Context mContext;
    private int NOTIFICATION_ID = 1;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private PendingIntent mContentIntent;
    private CharSequence mContentTitle;
    public NotificationHelper(Context context)
    {
	mContext = context;
    }

    /**
     * Put the notification into the status bar
     */
    public void createNotification() {
	//get the notification manager
	mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

	//create the notification
	int icon = android.R.drawable.stat_sys_upload;
	//        CharSequence tickerText = mContext.getString(R.string.download_ticker); //Initial text that appears in the status bar
	CharSequence tickerText = "Spicari: Uploading image..."; 
	long when = System.currentTimeMillis();
	mNotification = new Notification(icon, tickerText, when);

	//create the content which is shown in the notification pulldown
	//        mContentTitle = mContext.getString(R.string.content_title); //Full title of the notification in the pull down
	mContentTitle = "Spicari is uploading the image"; 
	//CharSequence contentText = "0% complete"; //Text of the notification in the pull down
	CharSequence contentText = " ";

	//you have to set a PendingIntent on a notification to tell the system what you want it to do when the notification is selected
	//I don't want to use this here so I'm just creating a blank one
	Intent notificationIntent = new Intent();
	mContentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

	//add the additional content and intent to the notification
	mNotification.setLatestEventInfo(mContext, mContentTitle, contentText, mContentIntent);

	//make this notification appear in the 'Ongoing events' section
	mNotification.flags = Notification.FLAG_ONGOING_EVENT;

	//show the notification
	mNotificationManager.notify(NOTIFICATION_ID, mNotification);

	//		NotificationCompat.Builder b = new NotificationCompat.Builder(mC)
	//		.setSmallIcon(com.coboltforge.sw.spicari.R.drawable.ic_launcher)
	//		.setContentTitle("Uploading image")
	//		.setContentText("Uploading " + "...");
	//
	//		// Creates an explicit intent for an Activity in your app
	//		Intent resultIntent = new Intent(mC, ImageSectionFragment.class);
	//
	//		// The stack builder object will contain an artificial back stack for the
	//		// started Activity.
	//		// This ensures that navigating backward from the Activity leads out of
	//		// your application to the Home screen.
	//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mC);
	//
	//		// Adds the back stack for the Intent (but not the Intent itself)
	//		stackBuilder.addParentStack(ImageSectionFragment.class);
	//		// Adds the Intent that starts the Activity to the top of the stack
	//		stackBuilder.addNextIntent(resultIntent);
	//		PendingIntent resultPendingIntent =
	//			stackBuilder.getPendingIntent(
	//				0,
	//				PendingIntent.FLAG_UPDATE_CURRENT
	//				);
	//
	//		b.setContentIntent(resultPendingIntent);
	//		NotificationManager mNotificationManager =
	//			(NotificationManager)  mC.getSystemService(Context.NOTIFICATION_SERVICE);
	//		mNotificationManager.notify(nID, b.build());

    }

    /**
     * Receives progress updates from the background task and updates the status bar notification appropriately
     * @param percentageComplete
     */
    public void progressUpdate(int percentageComplete) {
	//build up the new status message
	//	CharSequence contentText = percentageComplete + "% complete";
	CharSequence contentText = " ";
	//publish it to the status bar
	mNotification.setLatestEventInfo(mContext, mContentTitle, contentText, mContentIntent);
	mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * called when the background task is complete, this removes the notification from the status bar.
     * We could also use this to add a new ‘task complete’ notification
     */
    public void completed()    {
	//remove the notification from the status bar
	mNotificationManager.cancel(NOTIFICATION_ID);
    }
}

