package com.coboltforge.sw.sheduleloader;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class ComplexFTPTransfer  extends AsyncTask<Bitmap, Long[], Void>
{
    private FTPClient ftp = null;
    private Context mContext;
    private String TAG = "ComplexFTPTransfer";
    private int notificationID;
    private int mCompression = 100;
    private String  file;
    private NotificationHelper mNotificationHelper;


    public ComplexFTPTransfer(Context contex) {
	mContext = contex;
	mNotificationHelper = new NotificationHelper(contex);

    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();

	ftp = new FTPClient();
	try {
	    ftp.connect(InetAddress.getByName(Utils.getUserSettings(mContext, Constants.SERVER)));
	    ftp.login(Utils.getUserSettings(mContext, Constants.USER), Utils.getUserSettings(mContext, Constants.PW));
	    ftp.changeWorkingDirectory(Utils.getUserSettings(mContext, Constants.FOLDER));
	    ftp.setFileType(FTP.BINARY_FILE_TYPE);
	    ftp.enterLocalPassiveMode();



	} catch (SocketException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}


	Calendar c = Calendar.getInstance(); 
	int day = c.get(Calendar.DAY_OF_MONTH);
	int month = c.get(Calendar.MONTH) + 1; //stupid month starts with 0
	int year = c.get(Calendar.YEAR);


	file = year +"-" +month + "-"+ day + ".jpg";

	//Create the notification in the statusbar
	mNotificationHelper.createNotification();

    }

    @Override
    protected Void doInBackground(Bitmap... params) {
	if(!this.isCancelled())
	{
	    try 
	    {
		Bitmap image = params[0];
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, mCompression, stream);
		InputStream is = new ByteArrayInputStream(stream.toByteArray());

		//		publishProgress()
		//		int streamSize = 0;
		//		while(is.read() != -1)
		//		{
		//		    streamSize++;
		//		}
		//		Log.d(TAG, "stream size: " +streamSize);

		BufferedInputStream buffIn=new BufferedInputStream(is);

		Log.d(TAG, file);

		ftp.storeFile(file, buffIn);
	    }
	    catch (IOException e) {
	    }
	    catch (Exception e) {
	    }
	}
	return null;
    }

    @Override
    protected void onProgressUpdate(Long[]... values) {
	Log.d(TAG, values[0] + " of " + values[1] + " copied.");
	super.onProgressUpdate(values);


	//mNotificationHelper.progressUpdate(values[0]);

	//TODO Put code here
    }

    @Override
    protected void onPostExecute(Void result) {
	try {
	    ftp.logout();
	    ftp.disconnect();

	    Toast.makeText(mContext, "Done uploading, congrats bitch!", Toast.LENGTH_LONG).show();
	    Log.d("UPDATE", "logged out");
	    Log.d("UPDATE", "disconnected");

	} catch (IOException e) {
	    e.printStackTrace();
	}
	mNotificationHelper.completed();

	super.onPostExecute(result);
    }
}