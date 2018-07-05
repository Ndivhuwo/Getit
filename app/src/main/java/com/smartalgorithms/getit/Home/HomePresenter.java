package com.smartalgorithms.getit.Home;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
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
    private boolean googlesearch = true;

    public HomePresenter(Activity context, HomeContract.UiListener uiListener) {
        this.context = context;
        this.uiListener = uiListener;
        this.homeInteractor = new HomeInteractor(HomePresenter.this);
    }

    private boolean validate(String searchTerm) {
        if (searchTerm.length() <= 0) {
            uiListener.onValidateSearch(GeneralHelper.getString(R.string.text_invalid_search_request));
            return false;
        }
        return true;
    }

    public void onButtonClickSearch(String searchTerm, @Nullable LatLng currentLocation) {
        LoggingHelper.d(TAG, "onButtonClickSearch");
        if (validate(searchTerm)) {
            //Search request object
            int distance = GetitApplication.getSearchDistance() * 1000;
            SearchRequest searchRequest;
            if (currentLocation == null || !GetitApplication.useCurrentLocation())
                searchRequest = new SearchRequest(searchTerm);
            else
                searchRequest = new SearchRequest(searchTerm, currentLocation, distance);

            placeInfoArrayList = new ArrayList<>();
            if (!GeneralHelper.isInternetAvailable()) {
                GeneralHelper.displayToast(GeneralHelper.getString(R.string.error_body_internet_connection_required));
                uiListener.onSearchComplete(placeInfoArrayList);
            } else {
                if (currentLocation != null && GetitApplication.useCurrentLocation()) {
                    //homeInteractor.getInformationFB(searchRequest);
                    homeInteractor.getInformationGoogle(searchRequest);
                    //homeInteractor.getInformationTW(searchRequest);
                    //fbsearch = false;
                    //twsearch = false;
                    googlesearch = false;
                } else {
                    DialogInterface.OnClickListener ok_listener = (dialog, which) -> {
                        homeInteractor.getInformationFB(searchRequest);
                        //homeInteractor.getInformationGoogle(searchRequest);
                        //homeInteractor.getInformationTW(searchRequest);
                        fbsearch = false;
                        //twsearch = false;
                        //googlesearch = false;
                    };
                    DialogInterface.OnClickListener deny_listener = (dialog, which) -> {
                        uiListener.onSearchComplete(placeInfoArrayList);
                    };
                    uiListener.showMessage(GeneralHelper.getString(R.string.title_warning), GeneralHelper.getString(R.string.sentence_no_current_location), ok_listener, deny_listener, true, "YES", "NO");
                }
            }
        } else {
            uiListener.onSearchComplete(null);
        }
    }

    @Override
    public void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(context);
        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) || !rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(granted -> {
                        if(granted)
                            uiListener.requestLocation();
                        else {
                            DialogInterface.OnClickListener ok_listener = (dialog, which) -> requestPermissions();
                            DialogInterface.OnClickListener deny_listener = (dialog, which) -> uiListener.finishActivity();
                            uiListener.showMessage(GeneralHelper.getString(R.string.title_warning), GeneralHelper.getString(R.string.sentence_location_permissions_not_granted),
                                    ok_listener, deny_listener, true, GeneralHelper.getString(R.string.text_yes_caps), GeneralHelper.getString(R.string.text_no_caps));
                        }
                    });
        } else {
            uiListener.requestLocation();
        }
    }

    @Override
    public void onGetGooglePlaces(List<PlaceInfo> googlePlaces) {
        LoggingHelper.i(TAG, "onGetGooglePlaces");
        googlesearch = true;
        placeInfoArrayList.addAll(googlePlaces);
        loadAdapter();
    }

    @Override
    public void showMessage(String message) {
        uiListener.showErrorMessage(message);
    }


    public void requestAddress(LatLng coordinates) {
        if (!GeneralHelper.isInternetAvailable()) {
            GeneralHelper.displayToast(GeneralHelper.getString(R.string.error_body_internet_connection_required));
        } else {
            homeInteractor.getReverseGeocode(coordinates);
        }
    }

    @Override
    public void onGetAddress(ReverseGeoResponse reverseGeoResponse) {
        if (reverseGeoResponse.isSuccess()) {
            LoggingHelper.i(TAG, "Current Location: " + reverseGeoResponse.getResults().get(0).getFormatted_address());
            uiListener.onAddressRecieved(true, reverseGeoResponse.getResults().get(0).getFormatted_address());
        } else {
            LoggingHelper.e(TAG, "Error getting current location: " + reverseGeoResponse.getMessage());
            uiListener.onAddressRecieved(false, GeneralHelper.getString(R.string.text_default_location));
        }
    }

    @Override
    public void onGetFacebookPlace(List<PlaceInfo> facebookSearchResponse) {
        LoggingHelper.i(TAG, "onGetFacebookPlace");
        fbsearch = true;
        placeInfoArrayList.addAll(facebookSearchResponse);
        loadAdapter();
    }

    @Override
    public void onGetTweets(List<PlaceInfo> twitterSearchResponse) {
        LoggingHelper.i(TAG, "onGetTweets");
        twsearch = true;
        placeInfoArrayList.addAll(twitterSearchResponse);
        loadAdapter();
    }

    private void loadAdapter() {
        if (fbsearch && twsearch && googlesearch) {
            LoggingHelper.i(TAG, "Search Complete, List size: " + placeInfoArrayList.size());
            uiListener.onSearchComplete(placeInfoArrayList);
        } else {
            LoggingHelper.i(TAG, "Search still in progress");
        }
    }

    public void onButtonClickBack() {
        DialogInterface.OnClickListener ok_listener = (dialog, which) -> uiListener.finishActivity();
        DialogInterface.OnClickListener deny_listener = (dialog, which) -> {};
        uiListener.showMessage(GeneralHelper.getString(R.string.title_exit), GeneralHelper.getString(R.string.sentence_exit_app_confirmation),
                ok_listener, deny_listener, true, GeneralHelper.getString(R.string.text_yes_caps), GeneralHelper.getString(R.string.text_no_caps));
    }

    public void onButtonClickTryAgain() {
        if(!GeneralHelper.isInternetAvailable()) {
            showMessage(GeneralHelper.getString(R.string.sentence_no_internet_access));
        }else {
            uiListener.loadDefaultView();
            uiListener.requestLocation();
        }
    }
}
