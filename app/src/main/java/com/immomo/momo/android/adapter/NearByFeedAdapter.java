package com.immomo.momo.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.immomo.momo.android.BaseApplication;
import com.immomo.momo.android.BaseObjectListAdapter;
import com.immomo.momo.android.R;
import com.immomo.momo.android.entity.Entity;
import com.immomo.momo.android.entity.NearByFeed;
import com.immomo.momo.android.view.HandyTextView;

import java.util.List;

public class NearByFeedAdapter extends BaseObjectListAdapter {

	public NearByFeedAdapter(BaseApplication application, Context context,
							 List<? extends Entity> datas) {
		super(application, context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_notice, null);
			holder = new ViewHolder();



			holder.mHtvTitle = (HandyTextView) convertView
					.findViewById(R.id.notice_item_htv_title);
			holder.mLayoutGender = (LinearLayout) convertView
					.findViewById(R.id.notice_item_layout_gender);
			holder.mIvGender = (ImageView) convertView
					.findViewById(R.id.user_item_iv_gender);
			holder.mHtvContent = (HandyTextView) convertView
					.findViewById(R.id.notice_item_htv_content);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NearByFeed feed = (NearByFeed) getItem(position);
		holder.mHtvTitle.setText(feed.getNtitle());
		holder.mHtvContent.setText(feed.getContent() + "");

		return convertView;
	}

	class ViewHolder {

		HandyTextView mHtvTitle;
		LinearLayout mLayoutGender;
		ImageView mIvGender;
		HandyTextView mHtvContent;
	}
}
