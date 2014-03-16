package com.rajpriya.home.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.rajpriya.home.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajkumar on 3/8/14.
 */
public class Services implements Parcelable{

    public Services (Context context) {
        mNames.add(context.getString(R.string.title_section1));
        mNames.add(context.getString(R.string.title_section2));
        mNames.add(context.getString(R.string.title_section3));
        mNames.add(context.getString(R.string.title_section4));
        mNames.add(context.getString(R.string.title_section5));
        mNames.add(context.getString(R.string.title_section6));

        mUrls.add(context.getString(R.string.facebook_url));
        mUrls.add(context.getString(R.string.twitter_url));
        mUrls.add(context.getString(R.string.linkedin_url));
        mUrls.add(context.getString(R.string.google_plus_url));
        mUrls.add(context.getString(R.string.google_translate_url));
        mUrls.add(context.getString(R.string.google_Search_url));
    }

    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mUrls = new ArrayList<String>();

    public ArrayList<String> getUrls() {
        return mUrls;
    }

    public void setUrls(ArrayList<String> mUrls) {
        this.mUrls = mUrls;
    }

    public ArrayList<String> getNames() {
        return mNames;
    }

    public void setNames(ArrayList<String> mNames) {
        this.mNames = mNames;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
