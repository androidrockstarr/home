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

/**
 * Created by rajkumar on 3/9/14.
 */
public class WebAppAdatper extends BaseAdapter {
    private Context context;
    private ArrayList<String> mNames;
    private ArrayList<String> mUrls;


    public WebAppAdatper(Context context, ArrayList<String> names, ArrayList<String> urls) {
        this.context = context;
        mNames = names;
        mUrls = urls;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View root;

        //if (convertView == null) {

        root = new View(context);

        // get layout from mobile.xml
        root = inflater.inflate(R.layout.webapp, null);

        // set value into textview
        TextView textView = (TextView) root
                .findViewById(R.id.name);
        textView.setText(mNames.get(position));

        // set image based on selected text
        NetworkImageView imageView = (NetworkImageView) root
                .findViewById(R.id.icon);

        imageView.setImageUrl(mUrls.get(position) + "/favicon.ico", mImageLoader);

        //} else {
        //  gridView = (View) convertView;
        //}

        root.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                
                mNames.remove(position);
                mUrls.remove(position);
                notifyDataSetChanged();
                return true;
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, WebViewActivity.class);
                i.putExtra(WebViewActivity.WEB_URL, mUrls.get(position));
                i.putExtra(WebViewActivity.TITLE, mNames.get(position));
                context.startActivity(i);
            }
        });


        return root;
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

}
