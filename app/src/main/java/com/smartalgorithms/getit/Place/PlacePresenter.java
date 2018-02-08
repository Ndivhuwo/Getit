package com.smartalgorithms.getit.Place;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.Constants;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.Logger;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.FacebookPictureResponse;
import com.smartalgorithms.getit.Models.Local.PhotoData;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;
import com.smartalgorithms.getit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class PlacePresenter implements PlaceContract.PresenterListener {
    private static final String TAG = PlacePresenter.class.getSimpleName();
    private PlaceContract.UIListener uiListener;
    private PlaceInfo placeInfo;
    private PlaceInteractor placeInteractor;
    private List<String> imageIds = new ArrayList<>();
    private List<String> imageLinks = new ArrayList<>();

    public PlacePresenter(PlaceContract.UIListener uiListener, PlaceInfo placeInfo) {
        this.uiListener = uiListener;
        this.placeInfo = placeInfo;
        placeInteractor = new PlaceInteractor(PlacePresenter.this);
        placeInteractor.initialize();
    }


    @Override
    public void getPlaceImages(){
        for (PhotoData photo : placeInfo.getImageLinks()) {
            if(photo.getType() == Constants.PHOTO_DATA_TYPE_ID){
                imageIds.add(photo.getId());
            }else {
                imageLinks.add(photo.getId());
            }
        }
        if(imageIds.size() > 0){
            placeInteractor.getImageUrls(imageIds);
            imageLinks.clear();
        }else {
            uiListener.prepareGallery(imageLinks);
        }
    }

    @Override
    public void getUIStrings() {
        String title = placeInfo.getTitle().length() > 20 ? placeInfo.getTitle().substring(0,20) + "..." : placeInfo.getTitle();
        StringBuilder info = new StringBuilder();
        info.append("Name: " + placeInfo.getTitle() + "\n");
        if(placeInfo.getAbout() != null){
            info.append("About: " + placeInfo.getAbout() + "\n");
        }
        if(placeInfo.getDescription() != null){
            info.append("Description: " + placeInfo.getDescription());
        }
        String checkins = placeInfo.getCheckins() == 0 ? "Unknown Checkins" : placeInfo.getCheckins() + " Checkins";
        String link_phone = placeInfo.getPhone() == null ? placeInfo.getLink() : placeInfo.getPhone();

        uiListener.updateUI(title, info.toString(), link_phone, checkins);
    }

    @Override
    public void requestAddress() {
        if(!GeneralHelper.isInternetAvailable()){
            GeneralHelper.displayToast(GeneralHelper.getString(R.string.error_body_internet_connection_required));
        }
        else{
            if(placeInfo.getLongitude() != 0 && placeInfo.getLatitude() != 0) {
                LatLng coordinates = new LatLng(placeInfo.getLatitude(), placeInfo.getLongitude());
                placeInteractor.getReverseGeocode(coordinates);
            }

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
    public void onGetImageUrlsComplete(@Nullable List<FacebookPictureResponse> pictures) {
        if(pictures != null){
            for (FacebookPictureResponse pictureResponse : pictures){
                imageLinks.add(pictureResponse.getData().getUrl());
            }
        }else {
            Logger.e(TAG, "Error getting picture URLs from the ids");
        }
        uiListener.prepareGallery(imageLinks);
    }

    @Override
    public void onGetImageUrlsComplete(List<String> urls, int size) {
        if(urls != null){
            for (String pictureResponse : urls){
                imageLinks.add(pictureResponse);
            }
        }else {
            Logger.e(TAG, "Error getting picture URLs from the ids");
        }
        uiListener.prepareGallery(imageLinks);
    }
}
