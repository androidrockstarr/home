package com.rajpriya.home.utils;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by rajkumar on 3/29/14.
 */
public class PInfo {
    public String appname = "";
    public String pname = "";
    public String versionName = "";
    public int versionCode = 0;
    public Drawable icon;
    public double size;
    public void prettyPrint() {
        Log.e("rajpriya", appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
    }
}