package com.rajpriya.home.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajkumar on 3/8/14.
 */
public class Services implements Parcelable{

    public Services () {
        //
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
