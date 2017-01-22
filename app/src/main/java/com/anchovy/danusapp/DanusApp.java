package com.anchovy.danusapp;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by DarKnight060198 on 1/18/2017.
 */

public class DanusApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
