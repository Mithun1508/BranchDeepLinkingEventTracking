package com.example.myapplication;


import android.app.Application;
import android.provider.SyncStateContract;

import io.branch.referral.Branch;

public class CustomApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String customURL ="https://drive.google.com/drive/folders/1DJhn6qlqZZ4gqr3Plj9vq6u0A_wbC2pN?usp=drive_link";


        // Branch logging for debugging
        Branch.enableTestMode();

        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);
        Branch.getInstance().setRequestMetadata("custom_URL",customURL);
    }
}
