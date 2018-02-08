package com.smartalgorithms.getit.Home;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Helpers.Logger;
import com.smartalgorithms.getit.Helpers.NetworkHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;
import com.smartalgorithms.getit.Models.Local.SearchRequest;
import com.smartalgorithms.getit.PlaceUtility.PlaceProcessor;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class HomeInteractor {
    private static  final String TAG = HomeInteractor.class.getSimpleName();
    private HomeContract.PresenterListener presenterListener;
    private PlaceProcessor placeProcessor;

    public HomeInteractor(HomeContract.PresenterListener presenterListener) {
        this.presenterListener = presenterListener;
        placeProcessor = new PlaceProcessor();
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
                .subscribe(reverseGeoResponse -> presenterListener.onGetAddress(reverseGeoResponse),
                        error -> {
                        ReverseGeoResponse tmp = new ReverseGeoResponse();
                        tmp.setSuccess(false);
                        tmp.setMessage("Error: " + error.getMessage());
                        presenterListener.onGetAddress(tmp);
                    });
    }

    public void getInformationFB(SearchRequest searchRequest) {
        NetworkHelper.getInformationFBSingleObservable(searchRequest)
                .flatMap(facebookSearchResponse -> {
                    Logger.d(TAG, "getPlaceFromFB");
                    return placeProcessor.getPlaceFromFBObservable(facebookSearchResponse);
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
                    return placeProcessor.getPlaceFromTWObservable(twitterSearchResponse);
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
