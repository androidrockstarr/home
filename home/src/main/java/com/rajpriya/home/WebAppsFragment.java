package com.rajpriya.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rajpriya.home.utils.RecoWebAppsAdapter;
import com.rajpriya.home.utils.StoredServices;
import com.rajpriya.home.utils.StoredServices;
import com.rajpriya.home.utils.Utils;
import com.rajpriya.home.utils.WebAppAdatper;

import java.util.ArrayList;

/**
 * Created by rajkumar on 3/16/14.
 */

public class WebAppsFragment extends Fragment implements  AddServiceDialog.EditNameDialogListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ITEM_NAME = "DRAWER_ITEM_NAME";
    private static final String PREF_STORED_SERVICES = "rajpriya_stored_added_web_apps";

    private StoredServices mStoredServices;
    private ImageLoader mImageLoader;
    private GridView mAppGridGlobal;

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
            mStoredServices = gson.fromJson(str, StoredServices.class);
        else
            mStoredServices = new StoredServices(getActivity());

        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_web_apps, container, false);
        final GridView mAppGrid = (GridView)rootView.findViewById(R.id.appgrid);
        mAppGridGlobal = mAppGrid;
        mAppGrid.setAdapter(new WebAppAdatper(getActivity(),
                                             mStoredServices.getNames(),
                                             mStoredServices.getUrls(), mImageLoader));


        final LinearLayout tools = (LinearLayout)rootView.findViewById(R.id.tools);
        ImageView addButton = (ImageView)rootView.findViewById(R.id.add_service);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
            //Display service list fragment
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Make your selection");
                View rootView = inflater.inflate(R.layout.fragment_web_apps, container, false);
                final GridView appGrid = (GridView)rootView.findViewById(R.id.appgrid);
                appGrid.setSelector(R.drawable.selector_web_app_reco);
                LinearLayout tools = (LinearLayout)rootView.findViewById(R.id.tools);
                tools.setVisibility(View.GONE);
                appGrid.setAdapter(new RecoWebAppsAdapter(getActivity(), mImageLoader));
                builder.setView(rootView);
                builder.setPositiveButton("Add Selected", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<String> selectedUrls = ((RecoWebAppsAdapter)appGrid.getAdapter()).getSelectedUrls();
                        ArrayList<String> selectedNames = ((RecoWebAppsAdapter)appGrid.getAdapter()).getSelectedNames();
                        for (int j=0; j<selectedUrls.size(); j++) {
                            if (!mStoredServices.getUrls().contains(selectedUrls.get(j))) {
                                mStoredServices.getUrls().add(selectedUrls.get(j));
                                mStoredServices.getNames().add(selectedNames.get(j));

                            }
                        }
                        ((WebAppAdatper)mAppGrid.getAdapter()).notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Add New", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddServiceDialog dialog = new AddServiceDialog(WebAppsFragment.this);
                        dialog.show(getFragmentManager(), "dialog");
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

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

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        sp.edit().putString(PREF_STORED_SERVICES, gson.toJson(mStoredServices)).commit();

    }

    @Override
    public void onFinishEditDialog(String name, String url) {
        if(!mStoredServices.getUrls().contains(url)) {
            mStoredServices.getNames().add(name);
            mStoredServices.getUrls().add(url);
            ((WebAppAdatper)mAppGridGlobal.getAdapter()).notifyDataSetChanged();
        } else {
            //TODO: Alert that URL already added.
        }
    }
}
