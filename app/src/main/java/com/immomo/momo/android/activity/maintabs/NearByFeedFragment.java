package com.immomo.momo.android.activity.maintabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.immomo.momo.android.BaseApplication;
import com.immomo.momo.android.BaseFragment;
import com.immomo.momo.android.R;
import com.immomo.momo.android.activity.OtherProfileActivity;
import com.immomo.momo.android.adapter.NearByFeedAdapter;
import com.immomo.momo.android.entity.NearByFeed;
import com.immomo.momo.android.view.MoMoRefreshListView;
import com.immomo.momo.android.view.MoMoRefreshListView.OnCancelListener;
import com.immomo.momo.android.view.MoMoRefreshListView.OnRefreshListener;

import org.json.JSONObject;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NearByFeedFragment extends BaseFragment implements
		OnItemClickListener, OnRefreshListener, OnCancelListener {

	private MoMoRefreshListView mMmrlvList;
	private NearByFeedAdapter mAdapter;

	public NearByFeedFragment() {
		super();
	}

	public NearByFeedFragment(BaseApplication application, Activity activity,
							  Context context) {
		super(application, activity, context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_nearbyfeed, container,
				false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		mMmrlvList = (MoMoRefreshListView) findViewById(R.id.nearby_feed_mmrlv_list);
	}

	@Override
	protected void initEvents() {
		mMmrlvList.setOnItemClickListener(this);
		mMmrlvList.setOnRefreshListener(this);
		mMmrlvList.setOnCancelListener(this);
	}

	@Override
	protected void init() {
		getFeeds();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int position = (int) arg3;
		NearByFeed feed = mApplication.mNearByFeeds.get(position);
		String nid = null;
		String ntitle = null;
		String content = null;
		String site = null;
		if (position > 3) {
			nid = "momo_f_other";
		} else {
			nid = feed.getNid();
		}
		ntitle = feed.getNtitle();
		content = feed.getContent();
		site = feed.getSite();
		Intent intent = new Intent(mContext, OtherProfileActivity.class);
		intent.putExtra("nid", nid);
		intent.putExtra("ntitle", ntitle);
		intent.putExtra("content", content);
		intent.putExtra("site", site);
		startActivity(intent);
	}

	private void getFeeds() {
		if (mApplication.mNearByFeeds.isEmpty()) {
			putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					showLoadingDialog("正在加载,请稍后...");
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						String url = "http://10.10.4.14:8080/immomo_hd/noticeAction_getAllNoticesApp.action";
						HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

						InputStream inputStream = conn.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
						StringBuilder response = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null){
							response.append(line);//{"mesg":"success"}
						}

						JSONObject jsonObject = new Gson().fromJson(response.toString(),JSONObject.class);
						String str = jsonObject.get("noticeList").toString().replace("\"","");

						JSONArray array = new JSONArray(str);
						NearByFeed feed = null;
						JSONObject object = null;
						for (int i = 0; i < array.length(); i++) {
							object = array.getJSONObject(i);
							String nid = object.getString(NearByFeed.NID);
							String ntitle = object.getString(NearByFeed.NTITLE);
							String content = object.getString(NearByFeed.CONTENT);
							String site = object.getString(NearByFeed.SITE);

							feed = new NearByFeed(nid, ntitle, content, site);
							mApplication.mNearByFeeds.add(feed);
						}
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					dismissLoadingDialog();
					if (!result) {
						showCustomToast("数据加载失败...");
					} else {
						mAdapter = new NearByFeedAdapter(mApplication,
								mContext, mApplication.mNearByFeeds);
						mMmrlvList.setAdapter(mAdapter);
					}
				}

			});
		} else {
			mAdapter = new NearByFeedAdapter(mApplication, mContext,
					mApplication.mNearByFeeds);
			mMmrlvList.setAdapter(mAdapter);
		}
	}

	@Override
	public void onCancel() {
		clearAsyncTask();
		mMmrlvList.onRefreshComplete();
	}

	@Override
	public void onRefresh() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				mMmrlvList.onRefreshComplete();
			}
		});
	}

	public void onManualRefresh() {
		mMmrlvList.onManualRefresh();
	}
}
