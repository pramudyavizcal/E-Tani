package com.pramudya.e_tani;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;




public class BaseApp extends Application {
    private static BaseApp mInstance;

    public BaseApp() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    public static synchronized BaseApp getInstance() {
        return mInstance;
    }


}
