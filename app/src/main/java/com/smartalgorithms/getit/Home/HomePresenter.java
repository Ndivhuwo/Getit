package com.smartalgorithms.getit.Home;

import android.Manifest;
import android.app.Activity;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.Logger;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;
import com.smartalgorithms.getit.Models.Local.SearchRequest;
import com.smartalgorithms.getit.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class HomePresenter implements HomeContract.PresenterListener {
    private static String TAG = HomePresenter.class.getSimpleName();
    private Activity context;
    private HomeContract.UiListener uiListener;
    private HomeInteractor homeInteractor;
    private ArrayList<PlaceInfo> placeInfoArrayList;
    private boolean fbsearch = true;
    private boolean twsearch = true;

    public HomePresenter(Activity context, HomeContract.UiListener uiListener) {
        this.context = context;
        this.uiListener = uiListener;
        this.homeInteractor = new HomeInteractor(HomePresenter.this);
    }

    public void onButtonClickSearch(String searchTerm, LatLng currentLocation) {
        int distance = GetitApplication.getSearchDistance();
        SearchRequest searchRequest = currentLocation == null? new SearchRequest(searchTerm, distance):new SearchRequest(searchTerm, currentLocation, distance);
        placeInfoArrayList = new ArrayList<>();
        if(!GeneralHelper.isInternetAvailable()){
            GeneralHelper.displayToast(GeneralHelper.getString(R.string.error_body_internet_connection_required));
        }
        else{
            homeInteractor.getInformationFB(searchRequest);
            homeInteractor.getInformationTW(searchRequest);
            fbsearch = false;
            twsearch = false;
        }
    }

    @Override
    public void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(context);
        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) || !rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(granted -> {

                    });
        }
    }


    public void requestAddress(LatLng coordinates) {
        if(!GeneralHelper.isInternetAvailable()){
            GeneralHelper.displayToast(GeneralHelper.getString(R.string.error_body_internet_connection_required));
        }
        else{
            homeInteractor.getReverseGeocode(coordinates);
        }
    }

    @Override
    public void onGetAddress(ReverseGeoResponse reverseGeoResponse) {
        if(reverseGeoResponse.isSuccess()){
            uiListener.onAddressRecieved(reverseGeoResponse.getResults().get(0).getFormatted_address());
        }else {
            uiListener.onAddressRecieved(reverseGeoResponse.getMessage());
        }
    }

    @Override
    public void onGetFacebookPlace(List<PlaceInfo> facebookSearchResponse) {
        Logger.i(TAG, "onGetFacebookPlace");
        fbsearch = true;
        placeInfoArrayList.addAll(facebookSearchResponse);
        loadAdapter();
    }

    @Override
    public void onGetTweets(List<PlaceInfo> twitterSearchResponse) {
        Logger.i(TAG, "onGetTweets");
        twsearch = true;
        placeInfoArrayList.addAll(twitterSearchResponse);
        loadAdapter();
    }

    private void loadAdapter(){
        if(fbsearch && twsearch){
            Logger.i(TAG, "Search Complete, List size: " + placeInfoArrayList.size());
            uiListener.onSearchComplete(placeInfoArrayList);
        }else {
            Logger.i(TAG, "Search still in progress");
        }
    }

}
