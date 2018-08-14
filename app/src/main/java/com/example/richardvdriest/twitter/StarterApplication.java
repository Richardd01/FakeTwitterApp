package com.example.richardvdriest.twitter;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        //Vul Parse informatie in om te verbinden
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("yGL3DZAmXD29nbt7Z5Gc7y7BLCtJ70epClQuRsAr")
                .clientKey("Jfsak8IOVvKBfieUrKb5MRNF85aZKzJ9QaPPPDKP")
                .server("https://parseapi.back4app.com")
                .build());

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

}
