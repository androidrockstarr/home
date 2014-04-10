package com.rajpriya.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

/**
 * Created by rajkumar on 3/2/14.
 */

public class SettingsFragment extends Fragment {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Settings");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // May return null if EasyTracker has not yet been initialized with a property ID.
        Tracker easyTracker = EasyTracker.getInstance(getActivity());
        if (easyTracker != null) {
            // This screen name value will remain set on the tracker and sent with
            // hits until it is set to a new value or to null.
            easyTracker.set(Fields.SCREEN_NAME, "SettingsFragment");
            easyTracker.send(MapBuilder
                            .createAppView()
                            .build()
            );

        }
    }

}