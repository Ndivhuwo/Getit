package com.smartalgorithms.getit.Home;

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
    public interface UiListener{
        void onAddressRecieved(String address);
        void onSearchComplete(ArrayList<PlaceInfo> placeInfoArrayList);
    }

    public interface PresenterListener {
        void onGetAddress(ReverseGeoResponse reverseGeoResponse);
        void onGetFacebookPlace(List<PlaceInfo> facebookSearchResponse);
        void onGetTweets(List<PlaceInfo> twitterSearchResponse);
        void requestPermissions();
    }

}
