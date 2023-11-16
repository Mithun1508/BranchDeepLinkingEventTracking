package com.example.myapplication;


import android.app.Application;
import android.provider.SyncStateContract;

import io.branch.referral.Branch;

public class CustomApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // The custom URL to be associated with the Branch SDK.
        // You can use Google Drive or Dropbox to publish your app/apk
        // and use the generated link as the custom URL for your app on the Branch dashboard

        String customURL ="https://drive.google.com/drive/folders/1DJhn6qlqZZ4gqr3Plj9vq6u0A_wbC2pN?usp=drive_link";

        // Branch logging for debugging
        Branch.enableTestMode();

        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);

        // Set a custom request metadata, associating the custom URL with the Branch SDK
        Branch.getInstance().setRequestMetadata("custom_URL",customURL);
    }
}
