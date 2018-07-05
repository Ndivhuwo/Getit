package com.smartalgorithms.getit.Place;

import android.content.DialogInterface;

import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;

import java.util.List;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class PlaceContract {
    public interface UIListener {
        void onAddressRecieved(String formatted_address);

        void onAdapterCreated(PlaceImageAdapter imageLinks);

        void updateUI(String title, String info, String link_phone, String checkins);

        void showMessage(String title, String message, DialogInterface.OnClickListener ok_listener, DialogInterface.OnClickListener cancel_listener, boolean has_cancel, String positive_text, String negative_text);
        //void transitionOn(Class<?> toClass, finis)
    }

    public interface PresenterListener {
        void onGetAddress(ReverseGeoResponse reverseGeoResponse);

        void onGetImageUrlsComplete(List<String> urls);

        void getUIStrings();

        void requestAddress();
    }
}
