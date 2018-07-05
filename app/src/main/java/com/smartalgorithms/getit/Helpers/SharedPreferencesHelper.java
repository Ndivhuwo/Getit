package com.smartalgorithms.getit.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class SharedPreferencesHelper {
    private static final String TAG = SharedPreferencesHelper.class.getSimpleName();

    public static void setLocationRadius(Activity activity, int key, int raduisMeters) {
        LoggingHelper.d(TAG, "setLocationRadius");
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(GeneralHelper.getString(key), raduisMeters);
        editor.commit();
    }

    public static int getLocationRadius(Activity activity, int key) {
        LoggingHelper.d(TAG, "getLocationRadius");
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 10;
        return sharedPref.getInt(GeneralHelper.getString(key), defaultValue);
    }

    public static void setUseCurrentLocation(Activity activity, int key, boolean status) {
        LoggingHelper.d(TAG, "setUseCurrentLocation");
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(GeneralHelper.getString(key), status);
        editor.commit();
    }

    public static boolean getUseCurrentLocation(Activity activity, int key) {
        LoggingHelper.d(TAG, "getUseCurrentLocation");
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        boolean defaultValue = true;
        return sharedPref.getBoolean(GeneralHelper.getString(key), defaultValue);
    }
}
