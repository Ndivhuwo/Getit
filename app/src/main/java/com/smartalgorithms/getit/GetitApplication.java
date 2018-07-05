package com.smartalgorithms.getit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.smartalgorithms.getit.Helpers.LocationHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
import com.smartalgorithms.getit.Models.Local.TwitterAccessToken;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class GetitApplication extends Application{
    private static final String TAG = GetitApplication.class.getSimpleName();
    public static boolean LOGS_ENABLED = true;
    private static Context context;
    private static int searchDistance = Constants.DEFAULT_SEARCH_RADIUS;
    private static TwitterAccessToken twitterAccessToken = null;
    private static boolean useCurrentLocation = true;
    private static Intent location_intent;

    public static Context getAppContext() {
        if (context == null) {
            context = getAppContext();
        }
        return context;
    }

    public static Intent getLocationIntent() {
        return location_intent;
    }

    public static int getSearchDistance() {
        return searchDistance;
    }

    public static void setSearchDistance(int searchDistance) {
        GetitApplication.searchDistance = searchDistance;
    }

    public static boolean useCurrentLocation() {
        return useCurrentLocation;
    }

    public static void setUseCurrentLocation(boolean useCurrentLocation) {
        GetitApplication.useCurrentLocation = useCurrentLocation;
    }

    public static TwitterAccessToken getTwitterAccessToken() {
        return twitterAccessToken;
    }

    public static void setTwitterAccessToken(TwitterAccessToken twitterAccessToken) {
        GetitApplication.twitterAccessToken = twitterAccessToken;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        location_intent = new Intent(this, LocationHelper.class);
    }

}
