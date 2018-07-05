package com.smartalgorithms.getit.Home;

import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class HomeContract {
    public interface UiListener {
        void onAddressRecieved(boolean success, @Nullable String address);

        void onSearchComplete(ArrayList<PlaceInfo> placeInfoArrayList);

        void showMessage(String title, String message, DialogInterface.OnClickListener ok_listener, DialogInterface.OnClickListener cancel_listener, boolean has_cancel, String positive_text, String negative_text);

        void onValidateSearch(String message);

        void requestLocation();

        void finishActivity();

        void showErrorMessage(String message);

        void loadDefaultView();
    }

    public interface PresenterListener {
        void onGetAddress(ReverseGeoResponse reverseGeoResponse);

        void onGetFacebookPlace(List<PlaceInfo> facebookSearchResponse);

        void onGetTweets(List<PlaceInfo> twitterSearchResponse);

        void requestPermissions();

        void onGetGooglePlaces(List<PlaceInfo> googlePlaces);

        void showMessage(String string);
    }

}
