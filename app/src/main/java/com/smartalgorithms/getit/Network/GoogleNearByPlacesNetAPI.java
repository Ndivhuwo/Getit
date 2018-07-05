package com.smartalgorithms.getit.Network;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.BuildConfig;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
import com.smartalgorithms.getit.Models.Local.GoogleSearchResponse;
import com.smartalgorithms.getit.Models.Local.SearchRequest;
import com.smartalgorithms.getit.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class GoogleNearByPlacesNetAPI {
    private static final String TAG = GoogleNearByPlacesNetAPI.class.getSimpleName();

    private static GoogleSearchResponse getNearByPlaces(SearchRequest searchRequest){
        LoggingHelper.i(TAG, "getNearByPlaces");

        LoggingHelper.i(TAG, "current coordinates : lat: " + searchRequest.getLocation().latitude + " long: " + searchRequest.getLocation().longitude);
        HttpUrl.Builder httpUrlBuilder = new HttpUrl.Builder()
                .scheme("https")
                .host(GeneralHelper.getString(R.string.google_api_url))
                .addPathSegments("maps/api/place/nearbysearch/json")
                .addQueryParameter("location", searchRequest.getLocation().latitude + "," + searchRequest.getLocation().longitude)
                .addQueryParameter("radius", searchRequest.getSearchDistenceMeters()+"")
                .addQueryParameter("type", "restaurant|cafe")
                .addQueryParameter("key", BuildConfig.GoogleApiKey);

        if (!searchRequest.getSearchQuery().equalsIgnoreCase("any") && !searchRequest.getSearchQuery().equalsIgnoreCase("african")){
            httpUrlBuilder.addQueryParameter("keyword", searchRequest.getSearchQuery());
            httpUrlBuilder.addQueryParameter("name", searchRequest.getSearchQuery());
        }else if(searchRequest.getSearchQuery().equalsIgnoreCase("african")){
            httpUrlBuilder.addQueryParameter("keyword", "kasi");
            httpUrlBuilder.addQueryParameter("name", "shisanyama");
        }

        final HttpUrl url = httpUrlBuilder.build();
        Response response = NetworkUseCase.networkGET(url);
        GoogleSearchResponse googleSearchResponse = new GoogleSearchResponse();
        try {
            if (response.isSuccessful()){
                LoggingHelper.i(TAG, "getNearByPlaces success " );
                googleSearchResponse =googleSearchResponse.fromJson(response.body().string());
                googleSearchResponse.setSuccess(true);
            }else {
                LoggingHelper.i(TAG, "getNearByPlaces failed: "+ response.body().string());
                googleSearchResponse.setSuccess(false);
                googleSearchResponse.setMessage(response.body().string());
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            googleSearchResponse.setSuccess(false);
            googleSearchResponse.setMessage(ioe.getMessage() + " " + ioe.getCause());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            googleSearchResponse.setSuccess(false);
            googleSearchResponse.setMessage(nfe.getMessage() + " " + nfe.getCause());
        }

        return googleSearchResponse;
    }

    public static Single<GoogleSearchResponse> getNearByPlacesSingle(SearchRequest searchRequest) {
        return Single.defer(() -> Single.just(getNearByPlaces(searchRequest)));
    }
}
