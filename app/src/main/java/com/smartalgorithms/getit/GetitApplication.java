package com.smartalgorithms.getit;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.FacebookSdk;
import com.smartalgorithms.getit.Helpers.LocationHelper;
import com.smartalgorithms.getit.Models.Local.TwitterAccessToken;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalgorithms.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class GetitApplication extends Application{
    private static Context context;
    public static boolean LOGS_ENABLED=true;
    private static int searchDistance = Constants.DEFAULT_SEARCH_RADIUS;
    private static TwitterAccessToken twitterAccessToken = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Intent location_intent = new Intent(this, LocationHelper.class);
        startService(location_intent);
    }

    public static Context getAppContext(){
        if (context == null){
            context = getAppContext();
        }
        return context;
    }

    public static int getSearchDistance() {
        return searchDistance;
    }

    public static void setSearchDistance(int searchDistance) {
        GetitApplication.searchDistance = searchDistance;
    }

    public static TwitterAccessToken getTwitterAccessToken() {
        return twitterAccessToken;
    }

    public static void setTwitterAccessToken(TwitterAccessToken twitterAccessToken) {
        GetitApplication.twitterAccessToken = twitterAccessToken;
    }
}
