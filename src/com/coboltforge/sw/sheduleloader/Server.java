package com.coboltforge.sw.sheduleloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/*
 * http://geekjamboree.wordpress.com/2011/11/22/asynctask-call-web-services-in-android/
 * http://royvandewater.com/2010/10/uploading-a-file-to-ftp-with-android/
 */
public class Server extends AsyncTask<Context, Integer, String> {

	private OnPostExecuteListener        mPostExecuteListener = null;
	private Context mContext;
	public String hostname;
	public String user;
	public String folder;
	public String pass;
	public String port;
	public String file;
	private String error = null;
	private boolean connected = false;
	private FTPClient ftp = new FTPClient();

	public static interface OnPostExecuteListener{
		void onPostExecute(JSONObject json);
	}

	@Override
	protected String doInBackground(Context... params) {
		mContext = params[0];

		// TODO do stuff

		//get settings
		getUserSettings();

		if(connect()){
			upload(mContext);
		}
		close();

		return null;
	}

	private void getUserSettings() {
		// TODO Auto-generated method stub
		
	}

	private boolean close() {
		try {
			ftp.logout();
			ftp.disconnect();
			return true;
		} catch (IOException e) {
			error = e.getMessage();
			return false;
		}
	}

	private void upload(Context mContext2) {
		// TODO Auto-generated method stub

	}

	private boolean connect() {

		try {
			ftp.connect(InetAddress.getByName(hostname));
			ftp.login(user, pass);
			ftp.changeWorkingDirectory(folder);
			Log.v(Constants.TAG, "FTP Client: " + ftp.getReplyString());
			if (ftp.getReplyString().contains("250")) {
				connected = true;
			}

		} catch (SocketException e) {
			Log.e(MainActivity.TAG, e.getStackTrace().toString());
		} catch (UnknownHostException e) {
			Log.e(Constants.TAG, e.getStackTrace().toString());
		} catch (IOException e) {
			Log.e(Constants.TAG, e.getStackTrace().toString());
		}
		return connected;
	}


	@Override
	protected void onPostExecute(String result) {

	}


	public boolean upload(String localName, String remoteName)
	{
		/*

		ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
		BufferedInputStream buffIn = null;
		//				        buffIn = new BufferedInputStream(new FileInputStream(FULL_PATH_TO_LOCAL_FILE));
		//				        ftpClient.enterLocalPassiveMode();
		//				        ProgressInputStream progressInput = new ProgressInputStream(buffIn, progressHandler);
		//				 
		//				        boolean result = ftpClient.storeFile(localAsset.getFileName(), progressInput);
		//				        buffIn.close();
 */
		
		if(ftp.isConnected() && connected)
		{
			try {
				FileInputStream file = new FileInputStream(localName);
				boolean result = ftp.storeFile(remoteName, file);
				if(result) { return true; }
				else { return false; }
			} 
			catch (FileNotFoundException e) { error = e.getMessage(); return false; } 
			catch (IOException e) { error = e.getMessage(); return false; }
		}
		return false;
	}

	public String error()
	{
		return error;
	}
}
