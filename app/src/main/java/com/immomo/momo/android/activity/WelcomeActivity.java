package com.immomo.momo.android.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.immomo.momo.android.BaseActivity;
import com.immomo.momo.android.R;
import com.immomo.momo.android.activity.maintabs.MainTabActivity;
import com.immomo.momo.android.view.HandyTextView;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Map;

public class WelcomeActivity extends BaseActivity implements OnClickListener {

	private LinearLayout mLinearCtrlbar;
	private LinearLayout mLinearAvatars;
	private Button mBtnLogin;
	private ImageButton mIbtnAbout;

	private View[] mMemberBlocks;
	//附近的人，对应服务器http://localhost:8080/immomo_hd/userAction_getAllUsers.action
	private String[] mAvatars = new String[] { "welcome_0", "welcome_1",
			"welcome_2", "welcome_3", "welcome_4", "welcome_5" };
	private String[] mDistances ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		initViews();
		initEvents();
		initAvatarsItem();
		showWelcomeAnimation();

	}

	@Override
	protected void initViews() {
		mLinearCtrlbar = (LinearLayout) findViewById(R.id.welcome_linear_ctrlbar);
		mLinearAvatars = (LinearLayout) findViewById(R.id.welcome_linear_avatars);
		mBtnLogin = (Button) findViewById(R.id.welcome_btn_login);
		mIbtnAbout = (ImageButton) findViewById(R.id.welcome_ibtn_about);
	}

	@Override
	protected void initEvents() {
		mBtnLogin.setOnClickListener(this);
		mIbtnAbout.setOnClickListener(this);
	}

	private void initAvatarsItem() {

		initMemberBlocks();

		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			 @Override
			 protected void onPreExecute() {
				 super.onPreExecute();
				 showLoadingDialog("正在刷新,请稍后...");
			 }

			 @Override
			 protected Boolean doInBackground(Void... params) {
				 try {
					 String url = "http://10.10.4.14:8080/immomo_hd/userAction_getAllUsersApp.action";
					 HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

					 InputStream inputStream = conn.getInputStream();
					 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					 StringBuilder response = new StringBuilder();
					 String line;
					 while ((line = reader.readLine()) != null){
						 response.append(line);//{"mesg":"success"}
					 }

					 JSONObject jsonObject = new Gson().fromJson(response.toString(),JSONObject.class);
					 String str = jsonObject.get("userList").toString().replace("\"","");

					 StringBuffer sb = new StringBuffer();
					 for(int n=0;n<str.length();){
						 String[] strList = str.substring(n,str.indexOf("}")).split(",");
						 String[] str1 = strList[0].split(":");
						 sb.append(str1[1]+",");
						 n = str.indexOf("}")+2;
					 }

					 mDistances= sb.toString().split(",");
					 Handler mainHandler = new Handler(Looper.getMainLooper());
					 mainHandler.post(new Runnable() {
						 @Override
						 public void run() {
							 if(mDistances != null){
								 for (int i = 0; i < mDistances.length; i++) {
									 ((ImageView) mMemberBlocks[i]
											 .findViewById(R.id.welcome_item_iv_avatar))
											 .setImageBitmap(mApplication.getAvatar(mAvatars[i]));
									 ((HandyTextView) mMemberBlocks[i]
											 .findViewById(R.id.welcome_item_htv_distance))
											 .setText(mDistances[i]);
								 }
							 }
						 }
					 });
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
				if (result) {
					/*Intent intent = new Intent(WelcomeActivity.this,
							MainTabActivity.class);
					startActivity(intent);
					finish();*/
				} else {
					showCustomToast("联网失败！");
				}
			}
		 });



	}

	private void initMemberBlocks() {
		mMemberBlocks = new View[6];
		mMemberBlocks[0] = findViewById(R.id.welcome_include_member_avatar_block0);
		mMemberBlocks[1] = findViewById(R.id.welcome_include_member_avatar_block1);
		mMemberBlocks[2] = findViewById(R.id.welcome_include_member_avatar_block2);
		mMemberBlocks[3] = findViewById(R.id.welcome_include_member_avatar_block3);
		mMemberBlocks[4] = findViewById(R.id.welcome_include_member_avatar_block4);
		mMemberBlocks[5] = findViewById(R.id.welcome_include_member_avatar_block5);

		int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		int widthAndHeight = (mScreenWidth - margin * 12) / 6;
		for (int i = 0; i < mMemberBlocks.length; i++) {
			ViewGroup.LayoutParams params = mMemberBlocks[i].findViewById(
					R.id.welcome_item_iv_avatar).getLayoutParams();
			params.width = widthAndHeight;
			params.height = widthAndHeight;
			mMemberBlocks[i].findViewById(R.id.welcome_item_iv_avatar)
					.setLayoutParams(params);
		}
		mLinearAvatars.invalidate();
	}

	private void showWelcomeAnimation() {
		Animation animation = AnimationUtils.loadAnimation(
				WelcomeActivity.this, R.anim.welcome_ctrlbar_slideup);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mLinearAvatars.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mLinearAvatars.setVisibility(View.VISIBLE);
					}
				}, 800);
			}
		});
		mLinearCtrlbar.startAnimation(animation);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.welcome_btn_login:
			startActivity(LoginActivity.class);
			break;

		case R.id.welcome_ibtn_about:
			startActivity(AboutTabsActivity.class);
			break;
		}
	}

}