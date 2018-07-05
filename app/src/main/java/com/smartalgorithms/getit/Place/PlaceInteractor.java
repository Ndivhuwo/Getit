package com.smartalgorithms.getit.Place;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.BuildConfig;
import com.smartalgorithms.getit.Constants;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.NetworkHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.PhotoData;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;
import com.smartalgorithms.getit.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class PlaceInteractor {
    PlaceContract.PresenterListener presenterListener;
    PlaceInfo placeInfo;

    public PlaceInteractor(PlaceContract.PresenterListener presenterListener) {
        this.presenterListener = presenterListener;
        //initialize();
    }

    public void initialize(PlaceInfo placeInfo) {
        this.placeInfo = placeInfo;
        presenterListener.requestAddress();
        getImageUrls();
        presenterListener.getUIStrings();
    }

    public void getReverseGeocode(LatLng coordinates) {
        NetworkHelper.getAddressFromCoordinatesSingleObservable(coordinates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reverseGeoResponse -> {
                            //if(reverseGeoResponse.isSuccess()){
                            //Logger.d();
                            presenterListener.onGetAddress(reverseGeoResponse);
                            //}
                        }, error -> {
                            ReverseGeoResponse tmp = new ReverseGeoResponse();
                            tmp.setSuccess(false);
                            tmp.setMessage("Error: " + error.getMessage());
                            presenterListener.onGetAddress(tmp);
                        }
                );
    }

    public void getImageUrls() {
        List<String> imageIds = new ArrayList<>();
        List<String> imageLinks = new ArrayList<>();
        for (PhotoData photo : placeInfo.getImageLinks()) {
            if (photo.getType() == Constants.PHOTO_DATA_TYPE_ID) {
                imageIds.add(photo.getId());
            } else {
                imageLinks.add(photo.getId());
            }
        }
        if (imageIds.size() > 0) {
            List<String> urls = new ArrayList<>();
            for (String id : imageIds) {
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme("https")
                        .host(GeneralHelper.getString(R.string.facebook_search))
                        .addPathSegment("v2.11")
                        .addPathSegment(id)
                        .addPathSegment("picture")
                        .addQueryParameter("type", "normal")
                        .addQueryParameter("access_token", BuildConfig.FacebookAccessToken)
                        .build();
                urls.add(httpUrl.toString());
            }
            imageLinks.addAll(urls);
        }

        filterImages(imageLinks);
    }

    private void filterImages(List<String> imageLinks) {
        for (int i = 0; i < imageLinks.size(); i++) {
            if (imageLinks.get(i).contains("p50x50")) {
                imageLinks.remove(i);
            }
        }

        presenterListener.onGetImageUrlsComplete(imageLinks);
    }
}
