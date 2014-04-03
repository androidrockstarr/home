package com.rajpriya.home.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.rajpriya.home.R;
import com.rajpriya.home.WebViewActivity;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajkumar on 3/9/14.
 */
public class RecoWebAppsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mNames;
    private ArrayList<String> mUrls;
    private ImageLoader mImageLoader;
    private ArrayList<String> mSelectedNames;
    private ArrayList<String> mSelectedUrls;
    private Map<String, String> mMap = new HashMap<String, String>();

    public RecoWebAppsAdapter(Context context, ImageLoader imageLoader) {
        this.context = context;
        ReccomondedService rs = new ReccomondedService(context);
        mNames = rs.getNames();
        mUrls = rs.getUrls();
        mImageLoader = imageLoader;
        mSelectedNames = new ArrayList<String>();
        mSelectedUrls = new ArrayList<String>();

        //Map url n names
        for(int i=0; i<mNames.size(); i++) {
            String name = mNames.get(i);
            mMap.put(name, mUrls.get(i));
        }

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View root;

        if (convertView == null) {
            // get layout from mobile.xml
            convertView = inflater.inflate(R.layout.webapp, null);
        }

        // set value into textview
        TextView textView = (TextView) convertView
                .findViewById(R.id.name);
        textView.setText(mNames.get(position));
        textView.setTextColor(context.getResources().getColor(R.color.black));

        // set image based on selected text
        NetworkImageView imageView = (NetworkImageView) convertView
                .findViewById(R.id.icon);

        imageView.setImageUrl(mUrls.get(position) + "/favicon.ico", mImageLoader);
        imageView.setDefaultImageResId(R.drawable.webapp_default);
        imageView.setErrorImageResId(R.drawable.webapp_default);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(context.getResources().getColor(R.color.crystal_blue));
                mSelectedUrls.add(mUrls.get(position));
                mSelectedNames.add(mNames.get(position));
            }
        });


        return convertView;
    }


    @Override
    public int getCount() {
        return mUrls==null?0:mUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ArrayList<String> getSelectedNames() {
        return mSelectedNames;
    }

    public ArrayList<String> getSelectedUrls() {
        return mSelectedUrls;
    }

    public void sortAlphabetically1() {
        Collections.sort(mNames, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        correctUrlsOrder();
        notifyDataSetChanged();
    }

    public void correctUrlsOrder() {
        mUrls.clear();
        for (String name:mNames) {
            mUrls.add(mMap.get(name));
        }
    }
}
