package com.rajpriya.home;

/**
 * Created by rajkumar on 3/2/14.
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

// ...
public class AddServiceDialog extends DialogFragment implements TextView.OnEditorActionListener {

    public interface EditNameDialogListener {
        void onFinishEditDialog(String name, String url);
    }

    private EditText mEditTextName;
    private EditText mEditTextUrl;
    private WebAppsFragment mCaller;


    public AddServiceDialog(WebAppsFragment caller) {
        // Empty constructor required for DialogFragment
        mCaller  = caller;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_service_dfragment, container);
        mEditTextName = (EditText) view.findViewById(R.id.service_name);
        mEditTextUrl = (EditText) view.findViewById(R.id.service_url);

        getDialog().setTitle("Bookmark your favourite");

        // Show soft keyboard automatically
        mEditTextName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //mEditTextName.setOnEditorActionListener(this);
        // Show soft keyboard automatically
        //mEditTextUrl.requestFocus();
        mEditTextUrl.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // May return null if EasyTracker has not yet been initialized with a property ID.
        Tracker easyTracker = EasyTracker.getInstance(getActivity());
        if (easyTracker != null) {
            // This screen name value will remain set on the tracker and sent with
            // hits until it is set to a new value or to null.
            easyTracker.set(Fields.SCREEN_NAME, "AddServiceDialog");
            easyTracker.send(MapBuilder
                            .createAppView()
                            .build()
            );

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {

            try {
                URL url = new URL(mEditTextUrl.getText().toString());
                //
                // URLConnection conn = url.openConnection();
                //conn.connect();
            } catch (MalformedURLException e) {
                // the URL is not in a valid form
                //mEditTextUrl.append("    INVALID URL: Please correct it");
                Toast.makeText(getActivity(), "Entered URL seems invalid, please correct", Toast.LENGTH_LONG).show();
                return false;
            } catch (IOException e) {
                // the connection couldn't be established
                Toast.makeText(getActivity(), "Cant establish connection with this URL, please correct", Toast.LENGTH_LONG).show();
                return false;
            }

            // Return input text to activity

            mCaller.onFinishEditDialog(mEditTextName.getText().toString(), mEditTextUrl.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }
}