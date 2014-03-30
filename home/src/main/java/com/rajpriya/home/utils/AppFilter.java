package com.rajpriya.home.utils;

import android.widget.Filter;

import com.rajpriya.home.InstalledAppsActivity;

import java.util.ArrayList;

/**
 * Created by rajkumar on 3/29/14.
 */
public class AppFilter extends Filter {
    private ArrayList<PInfo> mApps;
    private InstalledAppsActivity.AppAdapter mAa;

    public AppFilter(ArrayList<PInfo> apps, InstalledAppsActivity.AppAdapter aa ) {
        mApps = new ArrayList<PInfo>();
        mApps.addAll(apps);
        mAa = aa;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        // We implement here the filter logic
        if (constraint == null || constraint.length() == 0) {
            // No filter implemented we return all the list
            results.values = mApps;
            results.count = mApps.size();
        }
        else {
            // We perform filtering operation
            ArrayList<PInfo> apps = new ArrayList<PInfo>();

            for (PInfo p : mApps) {
                if (p.appname.toUpperCase().startsWith(constraint.toString().toUpperCase())
                        ||
                        p.appname.toUpperCase().contains(constraint.toString().toUpperCase()))
                    apps.add(p);
            }

            results.values = apps;
            results.count = apps.size();

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
// Now we have to inform the adapter about the new list filtered
        if (filterResults.count == 0) {
            mAa.mApps.clear();
            mAa.notifyDataSetInvalidated();
        }
        else {
            //mApps = (ArrayList<PInfo>) filterResults.values;
            mAa.mApps.clear();
           mAa.mApps.addAll((ArrayList<PInfo>) filterResults.values);
            mAa.notifyDataSetChanged();
        }
    }
}
