package com.rajpriya.home.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by rajkumar on 3/8/14.
 */
public class Utils {

    public static void launchAppDetails(final Context context, final String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        context.startActivity(intent);

    }


    public static void launchApp(final Context context, final String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
        }

    }


    public static void deleteApp(final Context context, final String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:"+packageName));
        context.startActivity(intent);
    }

    public static void showActionDialog(final Context context, final String packageName) {
        final AlertDialog levelDialog;
        // Strings to Show In Dialog with Radio Buttons
        final CharSequence[] items = {" Launch  "," Details "," Remove  "};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Action");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        // Your code when first option seletced
                        Utils.launchApp(context, packageName);
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        Utils.launchAppDetails(context, packageName);
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        Utils.deleteApp(context, packageName);
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        break;

                }
                dialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }
}
