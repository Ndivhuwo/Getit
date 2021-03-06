package com.smartalgorithms.getit.Helpers;

import android.util.Log;

import com.smartalgorithms.getit.GetitApplication;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class LoggingHelper {
    public static void d(String tag, String message) {
        if (GetitApplication.LOGS_ENABLED) Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        if (GetitApplication.LOGS_ENABLED) Log.i(tag, message);
    }

    public static void e(String tag, String message) {
        if (GetitApplication.LOGS_ENABLED) Log.e(tag, message);
    }

    public static void v(String tag, String message) {
        if (GetitApplication.LOGS_ENABLED) Log.v(tag, message);
    }
}
