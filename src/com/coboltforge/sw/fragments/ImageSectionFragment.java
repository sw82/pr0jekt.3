package com.coboltforge.sw.fragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.coboltforge.sw.sheduleloader.ComplexFTPTransfer;
import com.coboltforge.sw.spicari.R;

/**
 * The image fragment 
 */
public class  ImageSectionFragment extends Fragment {
    File folder;
    private static  Uri mImageCaptureUri; // The current image URI
    private  Bitmap 	mImageBitmap;
    ImageView 	mImageView;	// Image view to show the current image.
    final int 	PICK_FROM_CAMERA = 42;
    Context 	mContex;
    static String 	TAG = "ImageSection";

    File photo;
    String mExtension=".png";
    Bitmap.CompressFormat bitmapFormat =  Bitmap.CompressFormat.PNG;

    private static  ProgressBar pb;

    public ImageSectionFragment() {
	
	
    }

    public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	Log.v(TAG, "onCreate Bitch: " );

	mContex = inflater.getContext();
	View root = inflater.inflate(R.layout.image, container, false);

	mImageView = (ImageView) root.findViewById(R.id.iv_photo);
	Button button_capture = (Button) root.findViewById(R.id.button_capture);


	if(getmImageCaptureUri()!=null){
	    mImageView.setImageURI(getmImageCaptureUri());
	}

	pb = (ProgressBar) root.findViewById(R.id.progressBar);

	button_capture.setOnClickListener(new View.OnClickListener() {

	    @Override
	    /*
	     * Take the image (non-Javadoc)
	     * @see android.view.View.OnClickListener#onClick(android.view.View)
	     */
	    public void onClick(View v) {
		captureImage();
	    }
	});

	Button button_upload = (Button) root.findViewById(R.id.button_upload);
	button_upload.setOnClickListener(new View.OnClickListener() {

	    @Override
	    /*
	     * Upload the image (non-Javadoc)
	     * @see android.view.View.OnClickListener#onClick(android.view.View)
	     */
	    public void onClick(View v) {

		try {
		    upload();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});  
	return root;
    }

    
    /**
     * Uploads a file
     * @throws Exception
     */
    protected void upload() throws Exception {

	//source:    http://stackoverflow.com/questions/8753919/track-ftp-upload-data-in-android?lq=1
	if(getmImageBitmap() != null){

	    ComplexFTPTransfer cft =    new ComplexFTPTransfer(mContex); 
	    cft.execute(mImageBitmap);
	}
	else{
	    if(mImageBitmap == null){
		try {
		    mImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageCaptureUri);
		    ComplexFTPTransfer cft =    new ComplexFTPTransfer(mContex); 
		    cft.execute(mImageBitmap);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	}
    }


    /**
     * capture an image
     * @return
     */
    private boolean captureImage() {
	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
	try
	{
	    // place where to store camera taken picture
	    photo = getTempFile(getActivity());
	    Log.v(TAG, "Photo Uri: " + photo.getPath() );
	}
	catch(Exception e)
	{
	    Log.v(TAG, "Can't create file to take picture! " +e.getMessage() );
	    Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
	    return false;
	}
	setmImageCaptureUri(Uri.fromFile(photo));
	Log.v(TAG, "Uri for intent:" + getmImageCaptureUri().toString() );
	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));

	startActivityForResult(intent, PICK_FROM_CAMERA);
	return true;

    }

    /*
     * create a temporary file
     */
    private File getTempFile(Context context){
	if	(context == null){
	    return null;
	}
	final File path = new File( Environment.getExternalStorageDirectory(), "spicari" );

	if(!path.exists()){
	    path.mkdir();
	    Log.v(TAG,"Creating new folder" + path.toString());
	}
	return new File(path, String.valueOf(getDate(System.currentTimeMillis())) + mExtension );
    }

    /**
     * Method for receiving a well formatted date out of a long {@link Long}.
     * @param milliSeconds
     * @return a well formatted date.
     */
    public static String getDate(long milliSeconds) {

	java.text.DateFormat formatter;

	formatter = SimpleDateFormat.getInstance();
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(milliSeconds);

	return formatter.format(calendar.getTime());
    }

    @Override  
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  
	Log.v(TAG, "request code:  " +requestCode);
	if (requestCode == PICK_FROM_CAMERA)   
	{  
	    if (resultCode == Activity.RESULT_OK)   
	    {  
		//get rid of loading shit
		setPB();
		

		setmImageBitmap(BitmapFactory.decodeFile(photo.getAbsolutePath()));
		mImageView.setImageBitmap(getmImageBitmap());
	    }  
	}  
	super.onActivityResult(requestCode, resultCode, data);
    }  

    private void setPB() {
	pb.setVisibility(View.GONE);
	
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
	Log.v(TAG, "supersavestate: ");
	Log.v(TAG, "" + outState.toString());

	super.onSaveInstanceState(outState);
	setUserVisibleHint(true);
    }

    //    private boolean saveBitmapToFile(Bitmap photo) throws IOException {
    //	@SuppressWarnings("unused")
    //	boolean mExternalStorageAvailable = false;
    //	@SuppressWarnings("unused")
    //	boolean mExternalStorageWriteable = false;
    //	String state = Environment.getExternalStorageState();
    //
    //	if (Environment.MEDIA_MOUNTED.equals(state)) {
    //	    mExternalStorageAvailable = mExternalStorageWriteable = true;
    //	    try {
    //		OutputStream fOut = null;
    //		String scheme = mImageCaptureUri.getScheme();
    //		String fileName = null;
    //
    //		if (scheme.equals("file")) {
    //		    fileName = mImageCaptureUri.getLastPathSegment();
    //		}
    //		File f = new File(folder, fileName);				
    //		fOut = new FileOutputStream(f);
    //		double size = photo.getWidth() * photo.getHeight();
    //		if (size> 0.5) {
    //		    photo.compress(bitmapFormat, compressRatio/2, fOut);
    //		}
    //		photo.compress(bitmapFormat, compressRatio, fOut);
    //
    //		fOut.flush();
    //		fOut.close();
    //
    //		MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),f.getAbsolutePath(),f.getName(),f.getName());
    //	    } catch (FileNotFoundException e) {
    //		e.printStackTrace();
    //	    }
    //
    //	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    //	    // We can only read the media
    //	    mExternalStorageAvailable = true;
    //	    mExternalStorageWriteable = false;
    //	} else {
    //	    // Something else is wrong. It may be one of many other states, but
    //	    // all we need
    //	    // to know is we can neither read nor write
    //	    mExternalStorageAvailable = mExternalStorageWriteable = false;
    //	}
    //	return false;
    //    }

    public static void setUri(Uri imageUri) {
	Log.v(ImageSectionFragment.TAG, "yeah - call from outside");

	pb.setVisibility(View.GONE);   //get rid of loading shit
	setmImageCaptureUri(imageUri);	

    }

    /**
     * @return the mImageBitmap
     */
    Bitmap getmImageBitmap() {
	return mImageBitmap;
    }

    /**
     * @param mImageBitmap the mImageBitmap to set
     */
    void setmImageBitmap(Bitmap mImageBitmap) {
	this.mImageBitmap = mImageBitmap;
    }

    /**
     * @return the mImageCaptureUri
     */
    private Uri getmImageCaptureUri() {
	return mImageCaptureUri;
    }

    /**
     * @param mImageCaptureUri the mImageCaptureUri to set
     */
    private static  void setmImageCaptureUri(Uri uri) {
	mImageCaptureUri = uri;
    }
}