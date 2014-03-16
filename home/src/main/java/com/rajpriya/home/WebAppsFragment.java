package com.rajpriya.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.rajpriya.home.utils.Services;
import com.rajpriya.home.utils.Utils;
import com.rajpriya.home.utils.WebAppAdatper;

/**
 * Created by rajkumar on 3/16/14.
 */

public class WebAppsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ITEM_NAME = "DRAWER_ITEM_NAME";
    private static final String PREF_STORED_SERVICES = "rajpriya_stored_added_web_apps";

    private Services mStoredServices;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WebAppsFragment newInstance(String name) {
        WebAppsFragment fragment = new WebAppsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, name);
        fragment.setArguments(args);

        return fragment;
    }

    public WebAppsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String str = sp.getString(PREF_STORED_SERVICES, null);
        if (!TextUtils.isEmpty(str))
            mStoredServices = gson.fromJson(str, Services.class);
        else
            mStoredServices = new Services(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_web_apps, container, false);
        GridView appGrid = (GridView)rootView.findViewById(R.id.appgrid);
        appGrid.setAdapter(new WebAppAdatper(getActivity(),
                                             mStoredServices.getNames(),
                                             mStoredServices.getUrls()));
        final LinearLayout tools = (LinearLayout)rootView.findViewById(R.id.tools);

        appGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                tools.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                tools.setVisibility(View.GONE);
            }
        });

        ((ImageView)rootView.findViewById(R.id.add_service)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ///TODO
            /*((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));*/
    }
}
