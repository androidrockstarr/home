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

}