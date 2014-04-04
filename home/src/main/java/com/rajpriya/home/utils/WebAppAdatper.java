package com.rajpriya.home.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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
public class WebAppAdatper extends BaseAdapter implements Filterable{
    private Context context;
    public ArrayList<String> mNames;
    public ArrayList<String> mUrls;
    private ImageLoader mImageLoader;
    private Filter mFilter;
    private Map<String, String> mMap = new HashMap<String, String>();


    public WebAppAdatper(Context context, ArrayList<String> names, ArrayList<String> urls, ImageLoader il) {
        this.context = context;
        mNames = names;
        mUrls = urls;
        mImageLoader = il;
        mFilter = new WebAppFilter(mNames, mUrls, this);
        //Map url n names
        for(int i=0; i<mNames.size(); i++) {
            String name = mNames.get(i);
            mMap.put(name, mUrls.get(i));
        }

    }

    public void refresh() {
        ((WebAppFilter)mFilter).refresh();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View root;

        if (convertView == null) {
            // get layout from mobile.xml
            convertView = inflater.inflate(R.layout.webapp, null);
        }

        Holder h = Holder.get(convertView);
        // set value into textview
        h.title.setText(mNames.get(position));

        // set image based on selected text
        h.icon.setImageUrl(mUrls.get(position) + "/favicon.ico", mImageLoader);
        h.icon.setDefaultImageResId(R.drawable.webapp_default);
        h.icon.setErrorImageResId(R.drawable.webapp_default);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Do you want to remove this app?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((WebAppFilter)mFilter).removeWebApp(mNames.get(position), mUrls.get(position));
                        mNames.remove(position);
                        mUrls.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, WebViewActivity.class);
                i.putExtra(WebViewActivity.WEB_URL, mUrls.get(position));
                i.putExtra(WebViewActivity.TITLE, mNames.get(position));
                context.startActivity(i);
            }
        });


        return convertView;
    }

    public void onNewWebAppAdded(String name, String url) {
        ((WebAppFilter)mFilter).onNewWebAppAdded(name, url);
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
    public void sortAlphabetically2() {
        Collections.sort(mNames, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s2.compareToIgnoreCase(s1);
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

    @Override
    public Filter getFilter() {
        if(mFilter == null)
            return new WebAppFilter(mNames, mUrls, this);
        else return mFilter;
    }


    static final class Holder {
        public final TextView title;
        public final NetworkImageView icon;

        Holder(View v) {
            title = (TextView) v.findViewById(R.id.name);
            icon = (NetworkImageView)v.findViewById(R.id.icon);
            v.setTag(this);
        }

        static Holder get(View v) {
            if (v.getTag() instanceof Holder) {
                return (Holder) v.getTag();
            }
            return new Holder(v);
        }
    }

}
