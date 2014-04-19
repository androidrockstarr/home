package com.rajpriya.home;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
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



        final int windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        final int windowheight = getWindowManager().getDefaultDisplay().getHeight();
        final ImageView img = (ImageView) findViewById(R.id.playicon);
        final ImageView close = (ImageView) findViewById(R.id.btn_close);


        img.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) img
                        .getLayoutParams();

                FrameLayout.LayoutParams layoutParamsClose = (FrameLayout.LayoutParams) close
                        .getLayoutParams();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        close.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        close.setVisibility(View.GONE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x_cord = (int) event.getRawX();
                        int y_cord = (int) event.getRawY();

                        if (x_cord > windowwidth) {
                            x_cord = windowwidth;
                        }
                        if (y_cord > windowheight) {
                            y_cord = windowheight;
                        }

                        layoutParams.leftMargin = x_cord - 25;
                        layoutParams.topMargin = y_cord - 75;

                        img.setLayoutParams(layoutParams);

                        if (x_cord >= (windowwidth - getResources().getDimensionPixelSize(R.dimen.image_size))
                            &&
                            y_cord >= (windowheight - getResources().getDimensionPixelSize(R.dimen.image_size))
                                ) {
                            WebViewActivity.this.finish();
                        }
                        break;
                    default:
                        break;
                }
                return true;
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

    @Override
    public void onStart() {
        super.onStart();
        // May return null if EasyTracker has not yet been initialized with a property ID.
        Tracker easyTracker = EasyTracker.getInstance(this);
        if (easyTracker != null) {
            // This screen name value will remain set on the tracker and sent with
            // hits until it is set to a new value or to null.
            easyTracker.set(Fields.SCREEN_NAME, "WebView Activity");
            easyTracker.send(MapBuilder
                            .createAppView()
                            .build()
            );

        }
    }
}
