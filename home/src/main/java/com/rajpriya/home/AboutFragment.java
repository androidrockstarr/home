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

import java.io.InputStream;

/**
 * Created by rajkumar on 4/5/14.
 */

public class AboutFragment extends Fragment {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        String data = "";
        try {
            InputStream in_s = getActivity().getResources().openRawResource(R.raw.help);
            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            data = new String(b);
            in_s.close();
        } catch (Exception e) {
            // e.printStackTrace();
            data = "Error loading data";
        }



        ((WebView)rootView.findViewById(R.id.webview)).loadDataWithBaseURL(
                null,
                data,
                "text/html",
                "UTF-8",
                null);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Help");
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
            easyTracker.set(Fields.SCREEN_NAME, "AboutFragment");
            easyTracker.send(MapBuilder
                            .createAppView()
                            .build()
            );

        }
    }
}