package com.moonly.chat;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dmitryi on 10/16/16.
 *  Main Application class of this app.
 */
public class MoonlyApp extends Application {
    private FirebaseDatabase database;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
