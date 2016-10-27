package se.dagensvimmerby;

import android.app.Application;

import se.dagensvimmerby.Middleware.ParseTools;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // PARSE
        ParseTools.setUpParse(getApplicationContext());
    }

}
