package com.rajpriya.home;

import com.rajpriya.home.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
