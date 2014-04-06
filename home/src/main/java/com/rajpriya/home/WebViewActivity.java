package com.rajpriya.home;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.rajpriya.home.admob.ToastAdListener;
import com.rajpriya.home.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class WebViewActivity extends Activity {

    WebView contentView = null;
    private ProgressBar progressbar;
    private AdView mAdView;

    private String mUrl;
    private String mName;

    public static final String WEB_URL = "MY_URL";
    public static final String TITLE = "MY_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getIntent() != null) {
            mUrl = getIntent().getStringExtra(WEB_URL);
            mName = getIntent().getStringExtra(TITLE);
        }

        if(savedInstanceState != null) {
            mUrl = savedInstanceState.getString(WEB_URL);
            mName = savedInstanceState.getString(TITLE);
        }

        if (TextUtils.isEmpty(mUrl)) {
            Toast.makeText(this, "URL of this webapp is invalid!!", Toast.LENGTH_LONG).show();
            finish();
        }


        setContentView(R.layout.activity_web_view);
        setTitle(mName);

        contentView = (WebView)findViewById(R.id.web_view);
        progressbar = (ProgressBar)findViewById(R.id.bar);


        // Create an ad.
        mAdView = new AdView(this);
        //mAdView.setAdListener(new ToastAdListener(this));
        mAdView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getResources().getString(R.string.ad_unit_id));

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ((LinearLayout) findViewById(R.id.webview_parent)).addView(mAdView, 0, params);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("2B5FCE7F5371A6FE3457055EA04FDA8E")
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        contentView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        contentView.getSettings().setJavaScriptEnabled(true);
        contentView.getSettings().setBuiltInZoomControls(true);
        contentView.getSettings().setSupportZoom(true);
        contentView.loadUrl(mUrl);
        contentView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressbar.getVisibility() == ProgressBar.GONE) {
                    progressbar.setVisibility(ProgressBar.VISIBLE);
                    //txtview.setVisibility(View.VISIBLE);
                }
                progressbar.setProgress(progress);
                if (progress == 100) {
                    progressbar.setVisibility(ProgressBar.GONE);
                    //txtview.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    public void onBackPressed () {
            if(contentView != null &&  contentView.canGoBack()) {
                contentView.goBack();
            } else
                super.onBackPressed();

    }
}
