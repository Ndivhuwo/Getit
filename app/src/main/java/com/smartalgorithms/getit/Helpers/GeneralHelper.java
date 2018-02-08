package com.smartalgorithms.getit.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smartalgorithms.getit.GetitApplication;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class GeneralHelper {

    public static boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) GetitApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            return true;//isOnline();
        } else
            return false;
    }

    public static String getString(int resourceId) {
        try {
            return GetitApplication.getAppContext().getResources().getString(resourceId);
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }

    public static String getMetadata(String key) {
        String results = null;

        try {
            ApplicationInfo ai = GetitApplication.getAppContext().getPackageManager().getApplicationInfo(GetitApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            results = (String)ai.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static int getColor(int resourceId) {
        try {
            return GetitApplication.getAppContext().getResources().getColor(resourceId);
        } catch (Resources.NotFoundException e) {
            return 0;
        }
    }

    public static Drawable getDrawable(int resourceId) {
        try {
            return GetitApplication.getAppContext().getResources().getDrawable(resourceId);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    public static String jsonFromObject(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Object objectFromJson(String json, Class<?> classType){
        Gson gson = new Gson();
        return gson.fromJson(json,classType);
    }

    public static void displayToast(String text){
        Toast.makeText(GetitApplication.getAppContext(), text, Toast.LENGTH_LONG).show();
    }
}
