package com.smartalgorithms.getit.Helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class LocationHelper extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static String TAG = LocationHelper.class.getSimpleName();
    private GoogleApiClient googleApiClient;
    private Context context;
    private LocationRequest locationRequest;
    private boolean retry = true;
    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                if (GeneralHelper.isLocationEnabled()) {
                    unregisterReceiver(this);
                    startLocationUpdates();
                }
            }
        }
    };

    private void startLocationUpdates() {
        LoggingHelper.d(TAG, "startLocationUpdates");
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();
    }

    private void stopLocationUpdates() {
        LoggingHelper.d(TAG, "stopLocationUpdates");
        googleApiClient.disconnect();
    }

    @SuppressWarnings("MissingPermission")
    private void requestLocationUpdates() {
        LoggingHelper.d(TAG, "requestLocationUpdates");
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, LocationHelper.this);
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
            if(retry) {
                retry = false;
                startLocationUpdates();
                SystemClock.sleep(5000);
            }
        }
    }

    private boolean checkPermissionLocation() {
        LoggingHelper.d(TAG, "checkPermissionLocation");
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LoggingHelper.d(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LoggingHelper.d(TAG, "onStartCommand");
        this.context = this;
        startLocationUpdates();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLocationChanged(Location location) {
        LoggingHelper.d(TAG, "onLocationChanged");
        Intent intent = new Intent("com.smartalgorithms.getit");
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("lng", location.getLongitude());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LoggingHelper.d(TAG, "onConnected");
        Location lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);

        if (lastLocation != null)
            onLocationChanged(lastLocation);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        if (checkPermissionLocation() && GeneralHelper.isLocationEnabled()) {
            requestLocationUpdates();
        } else {
            registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
