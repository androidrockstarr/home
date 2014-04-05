package com.rajpriya.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rajpriya.home.utils.PInfo;
import com.rajpriya.home.utils.RecoWebAppsAdapter;
import com.rajpriya.home.utils.StoredServices;
import com.rajpriya.home.utils.StoredServices;
import com.rajpriya.home.utils.Utils;
import com.rajpriya.home.utils.WebAppAdatper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by rajkumar on 3/16/14.
 */

public class WebAppsFragment extends Fragment implements  AddServiceDialog.EditNameDialogListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ITEM_NAME = "DRAWER_ITEM_NAME";
    private static final String PREF_STORED_SERVICES = "rajpriya_stored_added_web_apps";
    private static final String NUM_COLUMNS_WEB_APP = "number_of_columns_in_gridView_web_app";
    private static final String SORT_ORDER = "current_sort_order_of_grid_items";

    private StoredServices mStoredServices;
    private ImageLoader mImageLoader;
    private GridView mAppGridGlobal;
    private static int mNumGridCols;
    private boolean mSortReverseAlpha;
    private EditText mSearchBox;
    private LinearLayout mSearchPane;
    private TextView mButtonClose;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WebAppsFragment newInstance(String name) {

        WebAppsFragment fragment = new WebAppsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, name);
        fragment.setArguments(args);
        fragment.setHasOptionsMenu(true);

        return fragment;
    }

    public WebAppsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(savedInstance != null) {
            mNumGridCols = savedInstance.getInt(NUM_COLUMNS_WEB_APP, 3);
            mSortReverseAlpha = savedInstance.getBoolean(SORT_ORDER, false);
        } else {
            mNumGridCols = sp.getInt(NUM_COLUMNS_WEB_APP, 3);
            mSortReverseAlpha = sp.getBoolean(SORT_ORDER, false);
        }

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NUM_COLUMNS_WEB_APP, mNumGridCols);
        outState.putBoolean(SORT_ORDER, mSortReverseAlpha);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_web_apps, container, false);
        mAppGridGlobal = (GridView)rootView.findViewById(R.id.appgrid);
        mAppGridGlobal.setAdapter(new WebAppAdatper(getActivity(),
                                             mStoredServices.getNames(),
                                             mStoredServices.getUrls(), mImageLoader));


        sortAppsAlphabetically(mSortReverseAlpha, mAppGridGlobal);
        mAppGridGlobal.setNumColumns(mNumGridCols);
        mSearchBox = ((EditText)rootView.findViewById(R.id.search_box));
        mSearchPane = (LinearLayout)rootView.findViewById(R.id.search_panel);
        mButtonClose  = (TextView)rootView.findViewById(R.id.btn_close);


        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
                mSearchBox.clearFocus();
                mSearchBox.setText("");
                mSearchPane.setVisibility(View.GONE);

            }
        });

        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ((WebAppAdatper) mAppGridGlobal.getAdapter()).getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        mSearchBox.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(mSearchBox.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            return true;
                        }
                        return false;
                    }
                }
        );
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ///TODO
            /*((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));*/
        if(((ActionBarActivity)activity).getSupportActionBar() != null) {
            ((ActionBarActivity)activity).getSupportActionBar().setTitle("Bookmarks");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSearchPane.getVisibility() != View.VISIBLE) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            Gson gson = new Gson();
            sp.edit().putString(PREF_STORED_SERVICES, gson.toJson(mStoredServices)).commit();
            sp.edit().putInt(NUM_COLUMNS_WEB_APP, mNumGridCols).commit();
            sp.edit().putBoolean(SORT_ORDER, mSortReverseAlpha).commit();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.web_app, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_add:
                displayAddNewServiceDlg(getActivity().getLayoutInflater(), mAppGridGlobal);
                return true;
            case R.id.action_sort:
                if (mSortReverseAlpha) {
                    sortAppsAlphabetically(false, mAppGridGlobal);
                    mSortReverseAlpha = false;
                } else {
                    sortAppsAlphabetically(true, mAppGridGlobal);
                    mSortReverseAlpha = true;
                }
                return true;
            case R.id.action_grid_size:
                changeGridSize(mAppGridGlobal);
                return true;
            case R.id.action_search:
                displaySearchBox();
                return true;
            case R.id.action_refresh:
                refreshApps();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView () {
        //Make sure search box is empty before closing the fragment,
        //other wise all items are lost
        if (mSearchPane.getVisibility() == View.VISIBLE) {
            mSearchBox.setText("");
            mSearchPane.setVisibility(View.GONE);
        }

        super.onDestroyView();
    }

    @Override
    public void onFinishEditDialog(String name, String url) {
        if (TextUtils.isEmpty(url) ||  TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Name of URL is empty!", Toast.LENGTH_LONG).show();
            return;
        }
        if (mStoredServices.getUrls().contains(url)) {
            Toast.makeText(getActivity(), "This URL is already registered!", Toast.LENGTH_LONG).show();
            return;
        }
        if (mStoredServices.getNames().contains(name)) {
            Toast.makeText(getActivity(), "This Name is already registered!", Toast.LENGTH_LONG).show();
            return;
        }

        mStoredServices.getNames().add(name);
        mStoredServices.getUrls().add(url);
        ((WebAppAdatper)mAppGridGlobal.getAdapter()).notifyDataSetChanged();
        ((WebAppAdatper)mAppGridGlobal.getAdapter()).onNewWebAppAdded(name, url);

    }

    private void displayAddNewServiceDlg(LayoutInflater i, final GridView g) {
        if (mSearchPane.getVisibility() == View.VISIBLE) {
            mButtonClose.performClick();
        }

        //Display service list fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Make your selection");
        View rootView = i.inflate(R.layout.fragment_web_apps, null, false);
        rootView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        final GridView appGrid = (GridView)rootView.findViewById(R.id.appgrid);
        appGrid.setSelector(R.drawable.selector_web_app_reco);

        appGrid.setAdapter(new RecoWebAppsAdapter(getActivity(), mImageLoader));
        ((RecoWebAppsAdapter)appGrid.getAdapter()).sortAlphabetically1();
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
                        ((WebAppAdatper)mAppGridGlobal.getAdapter()).onNewWebAppAdded(selectedNames.get(j), selectedUrls.get(j));
                        ((WebAppAdatper)mAppGridGlobal.getAdapter()).notifyDataSetChanged();
                    }
                }
                ((WebAppAdatper)g.getAdapter()).notifyDataSetChanged();
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

    private void sortAppsAlphabetically(boolean reverse, GridView g) {
        if (reverse) {
            ((WebAppAdatper)g.getAdapter()).sortAlphabetically2();
        } else {
            ((WebAppAdatper)g.getAdapter()).sortAlphabetically1();
        }
        ((WebAppAdatper)g.getAdapter()).notifyDataSetChanged();

    }

    private void changeGridSize(final GridView g) {
        final CharSequence[] items = {" Increase "," Decrease "};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("App Grid Size");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        if (mNumGridCols > 1)
                            g.setNumColumns(--mNumGridCols);
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        g.setNumColumns(++mNumGridCols);
                        break;
                }
                dialog.dismiss();
            }
        });
        AlertDialog sortDialog = builder.create();
        sortDialog.show();
    }

    private void displaySearchBox() {
        if (mSearchPane.getVisibility() == View.VISIBLE) {
            return;
        }

        mSearchPane.setVisibility(View.VISIBLE);
        mSearchBox.requestFocus();
        InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mSearchBox.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

    }

    private void refreshApps() {
        ((WebAppAdatper)mAppGridGlobal.getAdapter()).refresh();
    }
}

