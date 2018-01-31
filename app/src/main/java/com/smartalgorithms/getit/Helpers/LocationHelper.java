package com.smartalgorithms.getit.Helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class LocationHelper extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static String TAG = LocationHelper.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private LocationRequest mLocationRequest;

    private void startLocationUpdates() {
        Logger.d(TAG, "startLocationUpdates");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    private void stopLocationUpdates() {
        Logger.d(TAG, "stopLocationUpdates");
        mGoogleApiClient.disconnect();
    }

    @SuppressWarnings("MissingPermission")
    private void requestLocationUpdates(){
        Logger.d(TAG, "requestLocationUpdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, LocationHelper.this);
    }

    private boolean checkPermissionLocation(){
        Logger.d(TAG, "checkPermissionLocation");
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(TAG, "onStartCommand");
        this.context = this;
        startLocationUpdates();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLocationChanged(Location location) {
        Logger.d(TAG, "onLocationChanged");
        Intent intent = new Intent("com.smartalgorithms.getit");
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("lng", location.getLongitude());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logger.d(TAG, "onConnected");

        Location lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if(lastLocation!=null)
            onLocationChanged(lastLocation);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        if (checkPermissionLocation()) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
