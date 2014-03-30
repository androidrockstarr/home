package com.rajpriya.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.rajpriya.home.admob.ToastAdListener;
import com.rajpriya.home.utils.AppFilter;
import com.rajpriya.home.utils.PInfo;
import com.rajpriya.home.utils.Utils;
import com.rajpriya.home.utils.WebAppAdatper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InstalledAppsActivity extends ActionBarActivity {
    private AdView mAdView;

    private  static ArrayList<PInfo>  mApps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_apps);

        //mApps = getPackages();
        mApps=new ArrayList<PInfo>();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.installed_apps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_full_screen) {
            if(getSupportActionBar() != null) {
                getSupportActionBar().hide();
                return true;
            } else {
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed () {
        if (!getSupportActionBar().isShowing()) {
            getSupportActionBar().show();
            //mNavigationDrawerFragment.setMenuVisibility(true);
        }
        else {
            super.onBackPressed();
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public  class PlaceholderFragment extends Fragment {
        private GridView mV;
        private LinearLayout mT;
        private Context c;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            c=getActivity();

            final View rootView = inflater.inflate(R.layout.fragment_installed_apps, container, false);
            final EditText box = ((EditText)rootView.findViewById(R.id.search_box));
            mV = ((GridView)rootView.findViewById(R.id.appgrid));
            mT = ((LinearLayout)rootView.findViewById(R.id.tools));
            mV.setAdapter(new AppAdapter(getActivity() , mApps));

            final AsyncTask task = new FetchAppListTask(getActivity(), mV).execute();

            // Create an ad.
            mAdView = new AdView(getActivity());
            mAdView.setBackgroundColor(getResources().getColor(R.color.white));
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(getActivity().getResources().getString(R.string.ad_unit_id));

            // Add the AdView to the view hierarchy. The view will have no size
            // until the ad is loaded.
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ((LinearLayout) rootView).addView(mAdView, 1, params);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device.
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("2B5FCE7F5371A6FE3457055EA04FDA8E")
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);


            ((ImageView)rootView.findViewById(R.id.btn_search)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rootView.findViewById(R.id.search_panel).setVisibility(View.VISIBLE);
                    ((EditText)rootView.findViewById(R.id.search_box)).requestFocus();
                    InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(box.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

                }
            });

            rootView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
                    box.clearFocus();
                    box.setText("");
                    rootView.findViewById(R.id.search_panel).setVisibility(View.GONE);

                }
            });

            ((EditText)rootView.findViewById(R.id.search_box)).addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    ((AppAdapter)mV.getAdapter()).getFilter().filter(cs);
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

            box.setOnEditorActionListener(
                    new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                    actionId == EditorInfo.IME_ACTION_DONE ||
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                in.hideSoftInputFromWindow(box.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                return true;
                            }
                            return false;
                        }
                    }
            );
            rootView.findViewById(R.id.grid_size).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CharSequence[] items = {" Increase "," Decrease "};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("App Grid Size");
                    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch(item)
                            {
                                case 0:
                                    // Your code when first option seletced
                                    mV.setNumColumns(mV.getNumColumns() - 1);
                                    break;
                                case 1:
                                    // Your code when 2nd  option seletced
                                    mV.setNumColumns(mV.getNumColumns() + 1);
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
                    AlertDialog sortDialog = builder.create();
                    sortDialog.show();
                }
            });

            rootView.findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CharSequence[] items = {" Alphabetically ",
                                                  " Alphabetically Reverse",
                                                  " App Size ",
                                                  " App Size Reverse"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Sort Apps By");
                    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch(item)
                            {
                                case 0:
                                      Collections.sort(mApps, new Comparator<PInfo>() {
                                          public int compare(PInfo result1, PInfo result2) {
                                              return result1.appname.compareTo(result2.appname);
                                          }
                                      });
                                      ((AppAdapter)mV.getAdapter()).notifyDataSetChanged();
                                    break;
                                case 1:
                                      Collections.sort(mApps, new Comparator<PInfo>() {
                                        public int compare(PInfo result1, PInfo result2) {
                                            return result2.appname.compareTo(result1.appname);
                                        }
                                      });
                                      ((AppAdapter)mV.getAdapter()).notifyDataSetChanged();
                                    break;
                                case 2:
                                    Collections.sort(mApps, new Comparator<PInfo>() {
                                        public int compare(PInfo result1, PInfo result2) {
                                            return result1.size >  result2.size ? 1 : -1;
                                        }
                                    });
                                    ((AppAdapter)mV.getAdapter()).notifyDataSetChanged();
                                    break;
                                case 3:
                                    Collections.sort(mApps, new Comparator<PInfo>() {
                                        public int compare(PInfo result1, PInfo result2) {
                                            return result1.size >  result2.size ? -1 : 1;
                                        }
                                    });
                                    ((AppAdapter)mV.getAdapter()).notifyDataSetChanged();
                                    break;
                                default:
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
                    AlertDialog sortDialog = builder.create();
                    sortDialog.show();
                }
            });

            return rootView;
        }
    }


    private ArrayList<PInfo> getPackages() {
        ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
        final int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }

            if(this.getPackageManager().getLaunchIntentForPackage(p.packageName) == null){
                //If you're here, then this is a not launch-able app
                continue;
            }


            PInfo newInfo = new PInfo();
            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
            File file = new File(p.applicationInfo.sourceDir);
            double size = file.length();  // size in Byte
            newInfo.size = size;


            res.add(newInfo);
        }
        return res;
    }


    public class AppAdapter extends BaseAdapter implements Filterable{
        private Context context;
        public ArrayList<PInfo> mApps;
        private Filter appFilter;


        public AppAdapter(Context context, ArrayList<PInfo> apps) {
            this.context = context;
            mApps = apps;
            appFilter = new AppFilter(mApps, this);

        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            //if (convertView == null) {

                gridView = new View(context);

                // get layout from mobile.xml
                gridView = inflater.inflate(R.layout.app, null);

                // set value into textview
                TextView textView = (TextView) gridView
                        .findViewById(R.id.name);
                textView.setText(mApps.get(position).appname);

                // set image based on selected text
                ImageView imageView = (ImageView) gridView
                        .findViewById(R.id.icon);

                imageView.setImageDrawable(mApps.get(position).icon);

            //} else {
              //  gridView = (View) convertView;
            //}

            gridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.showActionDialog(context, mApps.get(position).pname);
                }
            });

            return gridView;
        }

        @Override
        public int getCount() {
            return mApps.size();
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
            if(appFilter == null)
            return  new AppFilter(mApps, this);
            else return appFilter;
        }
    }


    public class FetchAppListTask extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog;
        private Context mContext;
        private GridView mV;

        public FetchAppListTask(Context context, GridView v) {
            mContext = context;
            dialog = new ProgressDialog(mContext);
            mV=v;
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            mApps = getPackages();
            return true;
        }
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Loading app list...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {



            if (dialog.isShowing())
                dialog.dismiss();

            mV.setAdapter(new AppAdapter(mContext , mApps));
            ((AppAdapter)mV.getAdapter()).notifyDataSetChanged();
        }

    }
    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
}
