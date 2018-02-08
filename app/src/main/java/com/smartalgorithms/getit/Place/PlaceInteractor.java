package com.smartalgorithms.getit.Place;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.NetworkHelper;
import com.smartalgorithms.getit.Models.Local.FacebookPictureResponse;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;
import com.smartalgorithms.getit.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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

    public PlaceInteractor(PlaceContract.PresenterListener presenterListener) {
        this.presenterListener = presenterListener;
        //initialize();
    }

    public void initialize() {
        presenterListener.requestAddress();
        presenterListener.getPlaceImages();
        presenterListener.getUIStrings();
    }

    public void getReverseGeocode(LatLng coordinates) {
        NetworkHelper.getAddressFromCoordinatesSingleObservable(coordinates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reverseGeoResponse ->{
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

    public void getImageUrls(List<String> imageIds) {
        /*
        Observable<List<String>> imageIdObservables = Observable.just(imageIds);
        imageIdObservables.flatMap(list ->
                Observable.fromIterable(list)
                        .map(item -> NetworkHelper.facebookGetPicture(item))
                        .toList()
                        .toObservable() // Required for RxJava 2.x
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(facebookPictureResponses -> presenterListener.onGetImageUrlsComplete(facebookPictureResponses),
                        error -> presenterListener.onGetImageUrlsComplete(null));
    */
        List<String> urls = new ArrayList<>();
        for(String id : imageIds) {
            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host(GeneralHelper.getString(R.string.facebook_search))
                    .addPathSegment("v2.11")
                    .addPathSegment(id)
                    .addPathSegment("picture")
                    .addQueryParameter("type", "normal")
                    .addQueryParameter("access_token", GeneralHelper.getString(R.string.facebook_access_token))
                    .build();
            urls.add(httpUrl.toString());
        }
        presenterListener.onGetImageUrlsComplete(urls, urls.size());
    }
}
