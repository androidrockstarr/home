package com.rajpriya.home;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rajpriya.home.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InstalledAppsActivity extends ActionBarActivity {

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            View rootView = inflater.inflate(R.layout.fragment_installed_apps, container, false);
            mV = ((GridView)rootView.findViewById(R.id.appgrid));
            mT = ((LinearLayout)rootView.findViewById(R.id.tools));
            mV.setAdapter(new AppAdapter(getActivity() , mApps));

            final AsyncTask task = new FetchAppListTask(getActivity(), mV).execute();

            ((ImageView)rootView.findViewById(R.id.order)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    if (view.isSelected()) {
                        view.setSelected(false);
                    Collections.sort(mApps, new Comparator<PInfo>() {
                        public int compare(PInfo result1, PInfo result2) {
                           return result1.appname.compareTo(result2.appname);
                        }
                    });
                    } else {
                        view.setSelected(true);
                        Collections.sort(mApps, new Comparator<PInfo>() {
                            public int compare(PInfo result1, PInfo result2) {
                                return result2.appname.compareTo(result1.appname);
                            }
                        });
                    }


                    ((AppAdapter)mV.getAdapter()).notifyDataSetChanged();
                    //return true;
                }
            });

            ((ImageView)rootView.findViewById(R.id.size)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.isSelected()) {
                        view.setSelected(false);
                        Collections.sort(mApps, new Comparator<PInfo>() {
                            public int compare(PInfo result1, PInfo result2) {
                                return result1.size >  result2.size ? 1 : -1;
                            }
                        });
                    } else {
                        view.setSelected(true);
                        Collections.sort(mApps, new Comparator<PInfo>() {
                            public int compare(PInfo result1, PInfo result2) {
                                return result1.size >  result2.size ? -1 : 1;
                            }
                        });
                    }


                    ((AppAdapter)mV.getAdapter()).notifyDataSetChanged();
                    //return true;
                }
            });

            ((ImageView)rootView.findViewById(R.id.zoomin)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mV.setNumColumns(mV.getNumColumns() - 1);
                    //return true;
                }
            });

            ((ImageView)rootView.findViewById(R.id.zoomout)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mV.setNumColumns(mV.getNumColumns() + 1);
                    //return true;
                }
            });

            ((ImageView)rootView.findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });





            return rootView;
        }
    }

    class PInfo {
        private String appname = "";
        private String pname = "";
        private String versionName = "";
        private int versionCode = 0;
        private Drawable icon;
        private double size;
        private void prettyPrint() {
            Log.e("rajpriya", appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
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


    public class AppAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<PInfo> mApps;


        public AppAdapter(Context context, ArrayList<PInfo> apps) {
            this.context = context;
            mApps = apps;

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

}
