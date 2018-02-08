package com.smartalgorithms.getit.Place;

import android.support.annotation.Nullable;

import com.smartalgorithms.getit.Models.Local.FacebookPictureResponse;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class PlaceContract {
    public interface UIListener{
        void onAddressRecieved(String formatted_address);
        void prepareGallery(List<String> imageLinks);
        void updateUI(String title, String info, String link_phone, String checkins);
        //void transitionOn(Class<?> toClass, finis)
    }

    public interface PresenterListener{
        void onGetAddress(ReverseGeoResponse reverseGeoResponse);
        void onGetImageUrlsComplete(@Nullable List<FacebookPictureResponse> pictures);
        void onGetImageUrlsComplete(List<String> urls, int size);
        void getPlaceImages();
        void getUIStrings();
        void requestAddress();
    }
}
