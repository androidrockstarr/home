package com.rajpriya.home.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.rajpriya.home.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by rajkumar on 3/8/14.
 */
public class StoredServices implements Parcelable{

    public StoredServices (Context context) {
        mNames.add(context.getString(R.string.title_section1));
        mNames.add(context.getString(R.string.title_section2));
        mNames.add(context.getString(R.string.title_section3));
        mNames.add(context.getString(R.string.title_section4));
        mNames.add(context.getString(R.string.title_section6));
        mNames.add("Yahoo!");
        mNames.add("Wikipedia ");
        mNames.add("Amazon ");
        mNames.add("Pinterest");
        mNames.add("BBC");
        mNames.add("CNN");
        mNames.add("Instagram");
        mNames.add("WhatsApp");

        mUrls.add(context.getString(R.string.facebook_url));
        mUrls.add(context.getString(R.string.twitter_url));
        mUrls.add(context.getString(R.string.linkedin_url));
        mUrls.add(context.getString(R.string.google_plus_url));
        mUrls.add(context.getString(R.string.google_Search_url));
        mUrls.add("http://m.yahoo.com");
        mUrls.add("http://m.wikipedia.com");
        mUrls.add("http://m.amazon.com");
        mUrls.add("http://m.pinterest.com");
        mUrls.add("http://m.bbc.com");
        mUrls.add("http://m.cnn.com");
        mUrls.add("http://instagram.com/m");
        mUrls.add("http://whatsapp.com/");

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
