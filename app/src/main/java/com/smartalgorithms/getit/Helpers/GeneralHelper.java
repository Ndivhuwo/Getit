package com.smartalgorithms.getit.Helpers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smartalgorithms.getit.GetitApplication;

import java.net.MalformedURLException;
import java.net.URL;

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

    public static boolean isLocationEnabled() {
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;

        //Check GPS provider
        if (locationManager == null)
            locationManager = (LocationManager) GetitApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Check network provider
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gps_enabled || network_enabled;
    }

    public static String getString(int resourceId) {
        try {
            return GetitApplication.getAppContext().getResources().getString(resourceId);
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }

    public static String[] getStringArray(int resourceId) {
        try {
            return GetitApplication.getAppContext().getResources().getStringArray(resourceId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMetadata(String key) {
        String results = null;

        try {
            ApplicationInfo ai = GetitApplication.getAppContext().getPackageManager().getApplicationInfo(GetitApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            results = (String) ai.metaData.get(key);
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

    public static String getJsonFromObject(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Object getObjectFromJson(String json, Class<?> classType) {
        Gson gson = new Gson();
        return gson.fromJson(json, classType);
    }

    public static void displayToast(String text) {
        Toast.makeText(GetitApplication.getAppContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void displayDialog(Context lContext, String title, String message, DialogInterface.OnClickListener ok_listener, DialogInterface.OnClickListener cancel_listener, boolean has_cancel, String positiveText, String negativeText) {

        AlertDialog.Builder builder = new AlertDialog.Builder(lContext);
        builder.setTitle(title);
        builder.setMessage(message);
        if (ok_listener == null) {
            builder.setPositiveButton(positiveText, ((dialog, which) -> {/*Do nothing*/}));
        } else
            builder.setPositiveButton(positiveText, ok_listener);

        if (has_cancel) {
            if (cancel_listener == null) {
                builder.setNegativeButton(negativeText, (dialog, arg1) -> {/*Do nothing*/ });
            } else
                builder.setNegativeButton(negativeText, cancel_listener);
        }
        builder.create().show();
    }

    public static boolean isURL(String imageLocation) {
        String[] parts = imageLocation.split("\\s+");
        for (String item : parts)
            try {
                URL url = new URL(item);
                return true;
            } catch (MalformedURLException e) {
                return false;
            }
        return false;
    }
}
