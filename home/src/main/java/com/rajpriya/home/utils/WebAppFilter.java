package com.rajpriya.home.utils;

import android.text.TextUtils;
import android.widget.Filter;
import java.util.ArrayList;

/**
 * Created by rajkumar on 3/29/14.
 */
public class WebAppFilter extends Filter {
    private ArrayList<String> mNames;
    private ArrayList<String> mUrls;
    private  WebAppAdatper mWwebAdapter;

    public WebAppFilter(ArrayList<String> names,  ArrayList<String> urls, WebAppAdatper waa ) {
        mNames = new ArrayList<String>();
        mNames.addAll(names);
        mUrls = new ArrayList<String>();
        mUrls.addAll(urls);
        mWwebAdapter = waa;
    }

    public void onNewWebAppAdded(String name, String url) {
        if (!TextUtils.isEmpty(url)) {
            mNames.add(name);
            mUrls.add(url);
        }
    }

    public void removeWebApp(String name, String url) {
        if (!TextUtils.isEmpty(url)) {
            mNames.remove(name);
            mUrls.remove(url);
        }
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        // We implement here the filter logic
        if (constraint == null || constraint.length() == 0) {
            // No filter implemented we return all the list
            results.values = mUrls;
            results.count = mUrls.size();
        }
        else {
            // We perform filtering operation
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<String> urls = new ArrayList<String>();

            for (String name : mNames) {
                if (name.toUpperCase().startsWith(constraint.toString().toUpperCase())
                        ||
                    name.toUpperCase().contains(constraint.toString().toUpperCase())) {
                    names.add(name);
                    urls.add(mUrls.get(mNames.indexOf(name)));
                }

            }

            results.values = urls;
            results.count = urls.size();

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
// Now we have to inform the adapter about the new list filtered
        if (filterResults.count == 0) {
            mWwebAdapter.mNames.clear();
            mWwebAdapter.mUrls.clear();
            mWwebAdapter.notifyDataSetInvalidated();
        }
        else {
            //mApps = (ArrayList<PInfo>) filterResults.values;
            mWwebAdapter.mUrls.clear();
            mWwebAdapter.mUrls.addAll((ArrayList<String>) filterResults.values);
            mWwebAdapter.mNames.clear();
            for(String url: mWwebAdapter.mUrls) {
                int index = mUrls.indexOf(url);
                mWwebAdapter.mNames.add(mNames.get(index));
            }
            mWwebAdapter.notifyDataSetChanged();
        }
    }
}

