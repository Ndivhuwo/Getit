package com.smartalgorithms.getit.Helpers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.Constants;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Models.Local.FacebookPictureResponse;
import com.smartalgorithms.getit.Models.Local.FacebookSearchResponse;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;
import com.smartalgorithms.getit.Models.Local.SearchRequest;
import com.smartalgorithms.getit.Models.Local.TwitterAccessToken;
import com.smartalgorithms.getit.Models.Local.TwitterSearchResponse;
import com.smartalgorithms.getit.R;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class NetworkHelper {
    public static String TAG = NetworkHelper.class.getSimpleName();

    private static Response generalCall(String url, int type, boolean encrypted, @Nullable String auth_string, @Nullable Object data, @Nullable String content_type){
        Logger.i(TAG, "general call");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .build();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (encrypted){
            builder.addHeader("Authorization", auth_string);
        }
        if(content_type != null){
            builder.addHeader("Content-Type", content_type);
        }
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        String jsonData = null;
        if(type == Constants.REQUEST_TYPE_POST || type == Constants.REQUEST_TYPE_PUT){
            jsonData = GeneralHelper.jsonFromObject(data);
        }
        switch (type){
            case Constants.REQUEST_TYPE_POST:
                builder.post(RequestBody.create(JSON,jsonData));
                break;
            case Constants.REQUEST_TYPE_PUT:
                builder.put(RequestBody.create(JSON, jsonData));
                //builder.
                break;
            case Constants.REQUEST_TYPE_GET:
                break;

        }
        Request request = builder.build();

        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    private static ReverseGeoResponse addressFromCoordinates(LatLng coordinates){
        Logger.d(TAG, "ReverseGeoResponse");
        ReverseGeoResponse reverseGeoResponse = new ReverseGeoResponse();
        Response response;

        String url = GeneralHelper.getString(R.string.google_geocode_url)+"?latlng="+ coordinates.latitude + "," + coordinates.longitude + "&key=" + GeneralHelper.getString(R.string.google_api_key);

        try {
            response = generalCall(url, Constants.REQUEST_TYPE_GET, false, null, null, null);
            String responseBody = response.body().string();
            Logger.d(TAG, "responseBody = "+responseBody);
            reverseGeoResponse = reverseGeoResponse.fromJson(responseBody);
            if(response.isSuccessful() && reverseGeoResponse!=null) {
                Logger.d(TAG, "reverse geocode success");
                reverseGeoResponse.setSuccess(true);
                return reverseGeoResponse;
            }
            else {
                Logger.d(TAG, "reverse geocode error");
                reverseGeoResponse.setSuccess(false);
                reverseGeoResponse.setMessage("reverse geocode error");
                return reverseGeoResponse;
            }
        }
        catch (IOException ioe){
            Logger.e(TAG, "reverse geocode  IO Exception: " + ioe.getMessage());
            ioe.printStackTrace();
            reverseGeoResponse.setSuccess(false);
            reverseGeoResponse.setMessage("reverse geocode IO Exception");
            return reverseGeoResponse;
        }
    }

    private static FacebookSearchResponse facebookPlaceSearch(SearchRequest searchRequest){
        Logger.i(TAG, "facebookPlaceSearch");
        FacebookSearchResponse facebookSearchResponse = new FacebookSearchResponse();
        Response response;

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(GeneralHelper.getString(R.string.facebook_search))
                .addPathSegment("v2.11")
                .addPathSegment("search")
                .addQueryParameter("type", "place")
                .addQueryParameter("q", searchRequest.getSearchQuery())
                .addQueryParameter("center", searchRequest.getLocation().latitude + ","+ searchRequest.getLocation().longitude)
                .addQueryParameter("distance", searchRequest.getSearchDistenceMeters()+"")
                .addQueryParameter("fields", "name,checkins,about,description,location,phone,link,picture,photos")
                .addQueryParameter("access_token", GeneralHelper.getString(R.string.facebook_access_token))
                .build();
        String url = httpUrl.toString();
        Logger.i(TAG, "URL: " + url);
        try {
            response = generalCall(url, Constants.REQUEST_TYPE_GET, false, null, null, null);
            String responseBody = response.body().string();
            Logger.i(TAG, "responseBody = "+responseBody);
            facebookSearchResponse = facebookSearchResponse.fromJson(responseBody);
            if(response.isSuccessful() && facebookSearchResponse!=null) {
                Logger.i(TAG, "facebook place search success");
                facebookSearchResponse.setSuccess(true);
                return facebookSearchResponse;
            }
            else {
                Logger.i(TAG, "facebook place search error");
                facebookSearchResponse.setSuccess(false);
                facebookSearchResponse.setMessage("facebook place search error: " + response.body().string());
                return facebookSearchResponse;
            }
        }
        catch (IOException ioe){
            Logger.e(TAG, "facebook place search  IO Exception: " + ioe.getMessage());
            ioe.printStackTrace();
            facebookSearchResponse.setSuccess(false);
            facebookSearchResponse.setMessage("facebook place search IO Exception");
            return facebookSearchResponse;
        }
    }

    public static FacebookPictureResponse facebookGetPicture(String fb_picture_id){
        Logger.i(TAG, "facebookPlaceSearch");
        FacebookPictureResponse facebookPictureResponse = new FacebookPictureResponse();
        Response response;

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(GeneralHelper.getString(R.string.facebook_search))
                .addPathSegment("v2.11")
                .addPathSegment(fb_picture_id)
                .addPathSegment("picture")
                .addQueryParameter("type", "normal")
                .addQueryParameter("access_token", GeneralHelper.getString(R.string.facebook_access_token))
                .build();
        String url = httpUrl.toString();
        Logger.i(TAG, "URL: " + url);
        try {
            response = generalCall(url, Constants.REQUEST_TYPE_GET, false, null, null, null);
            String responseBody = response.body().string();
            Logger.i(TAG, "responseBody = "+responseBody);
            facebookPictureResponse = facebookPictureResponse.fromJson(responseBody);
            if(response.isSuccessful() && facebookPictureResponse!=null) {
                Logger.i(TAG, "facebook get picture success");
                facebookPictureResponse.setSuccess(true);
                return facebookPictureResponse;
            }
            else {
                Logger.i(TAG, "facebook get picture error");
                facebookPictureResponse.setSuccess(false);
                facebookPictureResponse.setMessage("facebook get picture error: " + response.body().string());
                return facebookPictureResponse;
            }
        }
        catch (IOException ioe){
            Logger.e(TAG, "facebook get picture  IO Exception: " + ioe.getMessage());
            ioe.printStackTrace();
            facebookPictureResponse.setSuccess(false);
            facebookPictureResponse.setMessage("facebook get picture IO Exception");
            return facebookPictureResponse;
        }
    }

    public static TwitterSearchResponse twitterSearch(SearchRequest searchRequest){
        Logger.i(TAG, "twitterSearch");
        TwitterSearchResponse twitterSearchResponse = new TwitterSearchResponse();
        Response response;
        TwitterAccessToken twitterAccessToken = GetitApplication.getTwitterAccessToken();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(GeneralHelper.getString(R.string.twitter_search))
                .addPathSegments("1.1/search/tweets.json")
                .addQueryParameter("q", searchRequest.getSearchQuery())
                .addQueryParameter("geocode", searchRequest.getLocation().latitude + ","+ searchRequest.getLocation().longitude + "," +searchRequest.getSearchDistenceKM() + "km")
                .build();
        try {
            response = generalCall(url.toString(), Constants.REQUEST_TYPE_GET, true, "Bearer " + twitterAccessToken.getAccess_token(), null, "application/json");
            String responseBody = response.body().string();
            Logger.i(TAG, "responseBody = "+responseBody);
            twitterSearchResponse = twitterSearchResponse.fromJson(responseBody);
            if(response.isSuccessful() && twitterSearchResponse!=null) {
                Logger.i(TAG, "twitter search success");
                twitterSearchResponse.setSuccess(true);
                return twitterSearchResponse;
            }
            else {
                Logger.i(TAG, "twitter search error");
                twitterSearchResponse.setSuccess(false);
                twitterSearchResponse.setMessage("twitter search error: " + response.body().string());
                return twitterSearchResponse;
            }
        }
        catch (IOException ioe){
            Logger.e(TAG, "twitter search  IO Exception: " + ioe.getMessage());
            ioe.printStackTrace();
            twitterSearchResponse.setSuccess(false);
            twitterSearchResponse.setMessage("twitter search IO Exception");
            return twitterSearchResponse;
        }

    }

    private static TwitterAccessToken getTwitterToken(){
        TwitterAccessToken twitterAccessToken = new TwitterAccessToken();

        HttpUrl outhUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(GeneralHelper.getString(R.string.twitter_search))
                .addPathSegments("oauth2/token")
                .addQueryParameter("grant_type", "client_credentials")
                .build();
        try {
            String urlApiKey = URLEncoder.encode(GeneralHelper.getMetadata("twitter_api_key"), "UTF-8");
            String urlApiSecret = URLEncoder.encode(GeneralHelper.getMetadata("twitter_api_secret_key"), "UTF-8");
            String combined = urlApiKey + ":" + urlApiSecret;
            String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
            Logger.i(TAG, outhUrl.toString());
            Response response = generalCall(outhUrl.toString(), Constants.REQUEST_TYPE_POST, true, "Basic " + base64Encoded, new String("grant_type=client_credentials"), "application/x-www-form-urlencoded;charset=UTF-8");
            String responseBody = response.body().string();
            twitterAccessToken = twitterAccessToken.fromJson(responseBody);
            if (response.isSuccessful() && twitterAccessToken != null) {
                Logger.i(TAG, "twitter get client credentials success");
                twitterAccessToken.setSuccess(true);
                return twitterAccessToken;
            } else {
                Logger.i(TAG, "twitter get client credentials error");
                twitterAccessToken.setSuccess(false);
                twitterAccessToken.setMessage("twitter get client credentials error: " + response.body().string());
                return twitterAccessToken;
            }
            //Logger.i(TAG, "Response: " + responseBody);

        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
            Logger.i(TAG, "Get metada exception");
            twitterAccessToken.setSuccess(false);
            twitterAccessToken.setMessage("Get metada exception :" + uee.getMessage());
            return twitterAccessToken;
        } catch (IOException ioe) {
            Logger.i(TAG, "Http exception");
            ioe.printStackTrace();
            twitterAccessToken.setSuccess(false);
            twitterAccessToken.setMessage("Http IO Exception: " + ioe.getMessage());
            return twitterAccessToken;

        }
        //return null;
    }

    public static Single<ReverseGeoResponse> getAddressFromCoordinatesSingleObservable(LatLng coordinates) {
        Logger.i(TAG, "getAddressFromCoordinatesSingleObservable");
        return Single.defer(() -> Single.just(addressFromCoordinates(coordinates)));
    }

    public static Single<FacebookSearchResponse> getInformationFBSingleObservable(SearchRequest searchRequest) {
        Logger.i(TAG, "getInformationSingleObservable");
        return Single.defer(() -> Single.just(facebookPlaceSearch(searchRequest)));
        //return null;
    }

    public static Single<TwitterSearchResponse> getInformationTWSingleObservable(SearchRequest searchRequest) {
        Logger.i(TAG, "getInformationSingleObservable");
        return Single.defer(() -> Single.just(twitterSearch(searchRequest)));
        //return null;
    }

    public static Single<TwitterAccessToken> getTwitterTokenObservable(){
        return Single.defer(() -> Single.just(getTwitterToken()));
    }

    public static Single<FacebookPictureResponse> facebookGetPictureObservable(String fb_picture_id){
        return Single.defer(() -> Single.just(facebookGetPicture(fb_picture_id)));
    }

}
