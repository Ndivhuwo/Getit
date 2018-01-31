package com.smartalgorithms.getit.Home;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Helpers.Logger;
import com.smartalgorithms.getit.Helpers.NetworkHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;
import com.smartalgorithms.getit.Models.Local.SearchRequest;
import com.smartalgorithms.getit.PlaceUtility.PlaceInteractor;

import java.util.ArrayList;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/07.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/07.
 */

public class HomeInteractor {
    private static  final String TAG = HomeInteractor.class.getSimpleName();
    private HomeContract.PresenterListener presenterListener;
    private PlaceInteractor placeInteractor;

    public HomeInteractor(HomeContract.PresenterListener presenterListener) {
        this.presenterListener = presenterListener;
        placeInteractor = new PlaceInteractor();
        presenterListener.requestPermissions();
        checkTwitterAccessToken();
    }

    private void checkTwitterAccessToken() {
        NetworkHelper.getTwitterTokenObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accessToken -> {
                    //TODO store the access in a database
                    GetitApplication.setTwitterAccessToken(accessToken);
                },
                        error ->{
                            Logger.i(TAG, "Could not get Twitter access token");
                        });
    }

    public void getReverseGeocode(LatLng coordinates) {
        NetworkHelper.getAddressFromCoordinatesSingleObservable(coordinates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ReverseGeoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ReverseGeoResponse reverseGeoResponse) {
                        //if(reverseGeoResponse.isSuccess()){
                            //Logger.d();
                            presenterListener.onGetAddress(reverseGeoResponse);
                        //}
                    }

                    @Override
                    public void onError(Throwable e) {
                        ReverseGeoResponse tmp = new ReverseGeoResponse();
                        tmp.setSuccess(false);
                        tmp.setMessage("Error: " + e.getMessage());
                        presenterListener.onGetAddress(tmp);
                    }
                });
    }

    public void getInformationFB(SearchRequest searchRequest) {
        NetworkHelper.getInformationFBSingleObservable(searchRequest)
                .flatMap(facebookSearchResponse -> {
                    Logger.d(TAG, "getPlaceFromFB");
                    return placeInteractor.getPlaceFromFBObservable(facebookSearchResponse);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(FBPlaces -> {
                        presenterListener.onGetFacebookPlace(FBPlaces);
                    }, e -> {
                    ArrayList<PlaceInfo> tmp = new ArrayList<>();
                        Logger.e(TAG, "Error: " + e.getMessage());
                        presenterListener.onGetFacebookPlace(tmp);
                    });
    }

    public void getInformationTW(SearchRequest searchRequest) {
        NetworkHelper.getInformationTWSingleObservable(searchRequest)
                .flatMap(twitterSearchResponse -> {
                    Logger.d(TAG, "getPlaceFromTW");
                    return placeInteractor.getPlaceFromTWObservable(twitterSearchResponse);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placeInfoList -> {
                        //if(reverseGeoResponse.isSuccess()){
                        //Logger.d();
                        presenterListener.onGetTweets(placeInfoList);
                        //}
                    }, e -> {
                        ArrayList<PlaceInfo> tmp = new ArrayList<>();
                        Logger.e(TAG, "Error: " + e.getMessage());
                        presenterListener.onGetTweets(tmp);
                        //Logger.i(TAG, "Twitter Error: " + e.getMessage());
                    });
    }

}
