package com.immomo.momo.android.activity.maintabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.immomo.momo.android.R;
import com.immomo.momo.android.view.HandyTextView;
import com.immomo.momo.android.view.HeaderLayout;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NearByFeedsActivity extends TabItemActivity {

	private HeaderLayout mHeaderLayout;
	private NearByFeedFragment nFeedFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearbyfeeds);
		initViews();
		init();
		//initFeedsList();

	}


	@Override
	protected void initViews() {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.nearby_header);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		nFeedFragment = new NearByFeedFragment(mApplication, this, this);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.nearby_feedList_layout_content, nFeedFragment).commit();
	}



}
