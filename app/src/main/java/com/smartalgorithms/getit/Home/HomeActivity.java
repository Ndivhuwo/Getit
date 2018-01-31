package com.smartalgorithms.getit.Home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.Adapters.AdapterContract;
import com.smartalgorithms.getit.Adapters.PlaceInfoAdapter;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.Logger;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalgorithms.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class HomeActivity extends AppCompatActivity implements HomeContract.UiListener, AdapterContract.UIListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    @BindView(R.id.et_search_term) EditText et_search_term;
    @BindView(R.id.tv_search) TextView tv_search;
    @BindView(R.id.tv_location) TextView tv_location;
    @BindView(R.id.rv_places) RecyclerView rv_places;
    HomePresenter presenter;
    LatLng coordinates;
    PlaceInfoAdapter placeInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupUI();
        presenter = new HomePresenter(HomeActivity.this, HomeActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.smartalgorithms.getit"));

    }

    private void setupUI() {
        ButterKnife.bind(this);
        tv_search.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = view -> {
        switch (view.getId()){
            case R.id.tv_search:
                tv_search.setBackgroundColor(GeneralHelper.getColor(R.color.lightRed));
                presenter.onButtonClickSearch(et_search_term.getText().toString(), coordinates);
                break;
        }
    };
    //View.On

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(coordinates == null) {
                coordinates = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
                Logger.i("Broadcast receiver", "lat: "+ coordinates.latitude+" lang: "+ coordinates.longitude);
                presenter.requestAddress(coordinates);
            }else if(coordinates.longitude != bundle.getDouble("lng") && coordinates.latitude != bundle.getDouble("lat")){
                coordinates = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
                Logger.i("Broadcast receiver", "lat: "+ coordinates.latitude+" lang: "+ coordinates.longitude);
                presenter.requestAddress(coordinates);
            }

        }
    };

    @Override
    public void onAddressRecieved(String address) {
        tv_location.setText(address);
    }

    @Override
    public void onSearchComplete(ArrayList<PlaceInfo> placeInfoArrayList) {
        Logger.i(TAG, "onSearchComplete");
        tv_search.setBackgroundColor(GeneralHelper.getColor(R.color.red));
        placeInfoAdapter = new PlaceInfoAdapter(placeInfoArrayList, coordinates, HomeActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        rv_places.setLayoutManager(mLayoutManager);
        rv_places.setItemAnimator(new DefaultItemAnimator());
        rv_places.setAdapter(placeInfoAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.hearxgroup.patientmapper"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onTransition(Class<?> toClass) {
        Intent intent = new Intent(HomeActivity.this, toClass);

    }
}
