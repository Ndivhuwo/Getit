package com.smartalgorithms.getit.Home;

import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2017 hearX Group (Pty) Ltd. All rights reserved
 * Contact info@hearxgroup.com
 * Created by Ndivhuwo Nthambeleni on 2018/01/28.
 * Updated by Ndivhuwo Nthambeleni on 2018/01/28.
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
