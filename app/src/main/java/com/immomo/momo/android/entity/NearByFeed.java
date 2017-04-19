package com.immomo.momo.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class NearByFeed extends Entity implements Parcelable {
	public static final String NID = "nid";
	public static final String NTITLE = "ntitle";
	public static final String CONTENT = "content";
	public static final String SITE = "site";
	public static final String AVATAR = "avatar";
	private String nid;
	private String ntitle;
	private String content;
	private String site;
	private String avatar;

	public NearByFeed() {
		super();
	}

	public NearByFeed(String nid, String ntitle, String content, String site) {
		super();
		this.nid = nid;
		this.ntitle = ntitle;
		this.content = content;
		this.site = site;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getNtitle() {
		return ntitle;
	}

	public void setNtitle(String ntitle) {
		this.ntitle = ntitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nid);
		dest.writeString(ntitle);
		dest.writeString(content);
		dest.writeString(site);
	}

	public static final Creator<NearByFeed> CREATOR = new Creator<NearByFeed>() {

		@Override
		public NearByFeed createFromParcel(Parcel source) {
			NearByFeed feed = new NearByFeed();
			feed.setNid(source.readString());
			feed.setNtitle(source.readString());
			feed.setContent(source.readString());
			feed.setSite(source.readString());
			return feed;
		}

		@Override
		public NearByFeed[] newArray(int size) {
			return new NearByFeed[size];
		}
	};
}
