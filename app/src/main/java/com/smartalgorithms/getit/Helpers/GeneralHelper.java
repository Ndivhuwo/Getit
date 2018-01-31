package com.smartalgorithms.getit.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.smartalgorithms.getit.GetitApplication;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/07.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/07.
 */

public class GeneralHelper {

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public static String jsonFromObject(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Object objectFromJson(String json, Class<?> classType){
        Gson gson = new Gson();
        return gson.fromJson(json,classType);
    }
}
