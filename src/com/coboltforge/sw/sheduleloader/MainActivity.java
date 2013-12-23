package com.coboltforge.sw.sheduleloader;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

import com.coboltforge.sw.fragments.ImageSectionFragment;
import com.coboltforge.sw.fragments.NotificationSectionFragment;
import com.coboltforge.sw.fragments.ServerSectionFragment;
import com.coboltforge.sw.spicari.R;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static final String TAG = "spicari";


    //image tab
    private int startTab = 1;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_main);
	// Create the adapter that will return a fragment for each of the three primary sections
	// of the app.
	mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

	// Set up the action bar.
	final ActionBar actionBar = getActionBar();
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	// Set up the ViewPager with the sections adapter.
	mViewPager = (ViewPager) findViewById(R.id.pager);
	mViewPager.setAdapter(mSectionsPagerAdapter);

	// When swiping between different sections, select the corresponding tab.
	// We can also use ActionBar.Tab#select() to do this if we have a reference to the
	// Tab.
	mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
	    @Override
	    public void onPageSelected(int position) {
		actionBar.setSelectedNavigationItem(position);
	    }
	});

	// For each of the sections in the app, add a tab to the action bar.
	for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
	    // Create a tab with text corresponding to the page title defined by the adapter.
	    // Also specify this Activity object, which implements the TabListener interface, as the
	    // listener for when this tab is selected.
	    actionBar.addTab(
		    actionBar.newTab()
		    .setText(mSectionsPagerAdapter.getPageTitle(i))
		    .setTabListener(this));
	}

	//hide software keyboard by default
	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
	mViewPager.setCurrentItem(startTab);


	// Get intent, action and MIME type
	Intent intent = getIntent();
	String action = intent.getAction();
	String type = intent.getType();

	if (Intent.ACTION_SEND.equals(action) && type != null) {
	    if (type.startsWith("image/")) {
		handleSendImage(intent); // Handle single image being sent
	    }
	}
    }

    void handleSendImage(Intent intent) {
	Log.v(TAG,"handle send image bitch");
	Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
	if (imageUri != null) {
	    // Update UI to reflect image being shared
	    ImageSectionFragment.setUri(imageUri);
	}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_main, menu);
	return true;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	// When the given tab is selected, switch to the corresponding page in the ViewPager.
	mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

	public SectionsPagerAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int i) {
	    Fragment fragment; //new fragment
	    Bundle args = new Bundle();
	    switch (i) {
	    case 0: //server
		fragment = new ServerSectionFragment();
		args.putInt(ServerSectionFragment.ARG_SECTION_NUMBER, i + 1);
		fragment.setArguments(args);
		return fragment;
	    case 1: //image
		fragment = new ImageSectionFragment();
		args.putInt(ImageSectionFragment.ARG_SECTION_NUMBER, i + 1);
		fragment.setArguments(args);
		return fragment;
	    case 2: //notification
		fragment = new NotificationSectionFragment();
		args.putInt(NotificationSectionFragment.ARG_SECTION_NUMBER, i + 1);
		fragment.setArguments(args);
		return fragment;
	    }
	    return null;

	}

	@Override
	public int getCount() {
	    return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    switch (position) {
	    case 0: return getString(R.string.title_section1).toUpperCase(Locale.ENGLISH);
	    case 1: return getString(R.string.title_section2).toUpperCase(Locale.ENGLISH);
	    case 2: return getString(R.string.title_section3).toUpperCase(Locale.ENGLISH);
	    }
	    return null;
	}
    }


}