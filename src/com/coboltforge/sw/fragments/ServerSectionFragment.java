package com.coboltforge.sw.fragments;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.coboltforge.sw.sheduleloader.Constants;
import com.coboltforge.sw.sheduleloader.Utils;
import com.coboltforge.sw.spicari.R;

/**
 * The Server Fragment, responsible for the server settings
 */
public class  ServerSectionFragment extends Fragment {

    private String mServer;
    private String mUser;
    private String mPass;
    private String mPath;
    private String mPort;
    FTPClient ftpClient;
    Context mContext;

    EditText et_hostname;
    EditText et_port ;
    EditText et_user 	;
    EditText et_pass 	;
    EditText et_folder ;
    TextView tv_output;

    public ServerSectionFragment() {
    }

    public static final String ARG_SECTION_NUMBER = "section_number";
    protected static final String TAG = "ServerSection";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View root = inflater.inflate(R.layout.server, container, false);
	mContext = container.getContext();

	//get all layouts
	et_hostname 	= (EditText) root.findViewById(R.id.editText_hostname);
	et_port 	= (EditText) root.findViewById(R.id.editText_port);
	et_user 	= (EditText) root.findViewById(R.id.editText_user);
	et_pass 	= (EditText) root.findViewById(R.id.editText_password);
	et_folder 	= (EditText) root.findViewById(R.id.editText_folder);
	tv_output	= (TextView) root.findViewById(R.id.textViewOutput);
	tv_output.setMovementMethod(new ScrollingMovementMethod());
	// Array of choices
	String type[] = {"FTP"};

	// Selection of the spinner
	Spinner spinner = (Spinner) root.findViewById(R.id.spinner_type);

	// Application of the Array to the Spinner
	ArrayAdapter<String> spinnerArrayAdapter = 
		new ArrayAdapter<String>(getActivity(),   android.R.layout.simple_spinner_item, type);
	spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
	spinner.setAdapter(spinnerArrayAdapter);



	setTextHints();

	try {
	    //http://stackoverflow.com/questions/8706464/defaulthttpclient-to-androidhttpclient
	    if (android.os.Build.VERSION.SDK_INT > 9) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} 




	final Button connect = (Button) root.findViewById(R.id.button_connect);
	connect.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		ftpClient = new FTPClient();

		if (et_folder.getText().toString()!="") {
		    mPath =et_folder.getText().toString();
		}
		if (et_port.getText().toString()!="") {
		    mPort =  et_port.getText().toString();
		}
		if (et_hostname.getText().toString()!="") {
		    mServer =  et_hostname.getText().toString(); 

		    if (et_user.getText().toString()!="") {
			mUser = et_user.getText().toString();
			if (et_pass.getText().toString()!="") {
			    mPass =  et_pass.getText().toString();

			    Utils.saveUserSettings(mContext, Constants.SERVER, mServer);
			    Utils.saveUserSettings(mContext, Constants.USER, mUser);
			    Utils.saveUserSettings(mContext, Constants.PW, mPass);
			    Utils.saveUserSettings(mContext, Constants.PORT, mPort);
			    Utils.saveUserSettings(mContext, Constants.FOLDER, mPath);

			    try {
				tv_output.append("Trying to connect...\n");
				ftpClient.connect(InetAddress.getByName(Utils.getUserSettings(mContext, Constants.SERVER)));
				tv_output.append(ftpClient.getReplyString());
				ftpClient.login(Utils.getUserSettings(mContext, Constants.USER), Utils.getUserSettings(mContext, Constants.PW));
				tv_output.append(ftpClient.getReplyString());
				ftpClient.changeWorkingDirectory(Utils.getUserSettings(mContext, Constants.FOLDER));
				tv_output.append(ftpClient.getReplyString());

				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				tv_output.append(ftpClient.getReplyString());
				ftpClient.enterLocalPassiveMode();
				tv_output.append(ftpClient.getReplyString());

				ftpClient.logout();
				tv_output.append(ftpClient.getReplyString());
				ftpClient.disconnect();

				tv_output.append("Connection established.\n");

			    } catch (SocketException e) {
				e.printStackTrace();
			    } catch (UnknownHostException e) {
				e.printStackTrace();
			    } catch (IOException e) {
				e.printStackTrace();
			    }
			}
		    }	
		}
	    }
	});
	return root;
    }

    /**
     * restore old values if given in user settings - as a hint 
     */
    private void setTextHints() {
	if(Utils.getUserSettings(mContext, Constants.SERVER) !=null){
	    et_hostname.setText(Utils.getUserSettings(mContext, Constants.SERVER));
	    et_hostname.setHint(Utils.getUserSettings(mContext, Constants.SERVER));
	}
	if(Utils.getUserSettings(mContext, Constants.USER) !=null){
	    et_user.setText(Utils.getUserSettings(mContext, Constants.USER));
	    et_user.setHint(Utils.getUserSettings(mContext, Constants.USER));
	}
	if(Utils.getUserSettings(mContext, Constants.USER) !=null){
	    et_pass.setText(Utils.getUserSettings(mContext, Constants.PW));
	    et_pass.setHint(Utils.getUserSettings(mContext, Constants.PW));
	}
	if(Utils.getUserSettings(mContext, Constants.FOLDER) !=null){
	    et_folder.setText(Utils.getUserSettings(mContext, Constants.FOLDER));
	    et_folder.setHint(Utils.getUserSettings(mContext, Constants.FOLDER));
	}

    }

    static ServerSectionFragment newInstance() {
	return new ServerSectionFragment();
    }


}