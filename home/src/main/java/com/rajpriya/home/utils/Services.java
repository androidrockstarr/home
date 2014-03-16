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
        mNames.add("Yahoo!");
        mNames.add("Baidu ");
        mNames.add("Wikipedia ");
        mNames.add("Amazon ");
        mNames.add("Bing");
        mNames.add("VKontakte");
        mNames.add("eBay");
        mNames.add("tumblr");
        mNames.add("Pinterest");
        mNames.add("Paypal");
        mNames.add("IMDB");
        mNames.add("BBC");
        mNames.add("youku");
        mNames.add("flickr");
        mNames.add("Rakuten");
        mNames.add("CNN");
        mNames.add("Instagram");

        mUrls.add(context.getString(R.string.facebook_url));
        mUrls.add(context.getString(R.string.twitter_url));
        mUrls.add(context.getString(R.string.linkedin_url));
        mUrls.add(context.getString(R.string.google_plus_url));
        mUrls.add(context.getString(R.string.google_translate_url));
        mUrls.add(context.getString(R.string.google_Search_url));

        mUrls.add("http://m.yahoo.com");
        mUrls.add("http://m.baidu.com");
        mUrls.add("http://m.wikipedia.com");
        mUrls.add("http://m.amazon.com");
        mUrls.add("http://m.bing.com");
        mUrls.add("http://m.vk.com");
        mUrls.add("http://m.ebay.com");
        mUrls.add("http://m.tumblr.com");
        mUrls.add("http://m.pinterest.com");
        mUrls.add("http://m.aypal.com");
        mUrls.add("http://m.imdb.com");
        mUrls.add("http://m.bbc.com");
        mUrls.add("http://m.youku.com");
        mUrls.add("http://m.flickr.com");
        mUrls.add("http://m.rakuten.com");
        mUrls.add("http://m.cnn.com");
        mUrls.add("http://m.instagram.com");

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
