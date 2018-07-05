package com.smartalgorithms.getit.Home;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
import com.smartalgorithms.getit.Helpers.NetworkHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.FacebookSearchResponse;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;
import com.smartalgorithms.getit.Models.Local.SearchRequest;
import com.smartalgorithms.getit.Network.GoogleNearByPlacesNetAPI;
import com.smartalgorithms.getit.PlaceUtility.PlaceProcessor;
import com.smartalgorithms.getit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class HomeInteractor {
    private static final String TAG = HomeInteractor.class.getSimpleName();
    private HomeContract.PresenterListener presenterListener;
    private PlaceProcessor placeProcessor;

    public HomeInteractor(HomeContract.PresenterListener presenterListener) {
        this.presenterListener = presenterListener;
        placeProcessor = new PlaceProcessor();
        this.presenterListener.requestPermissions();
        checkInternetConnection();
        checkTwitterAccessToken();
    }

    private void checkInternetConnection() {
        if(!GeneralHelper.isInternetAvailable()) {
            presenterListener.showMessage(GeneralHelper.getString(R.string.sentence_no_internet_access));
        }
    }

    private void checkTwitterAccessToken() {
        NetworkHelper.getTwitterTokenObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accessToken -> {
                            //TODO store the access in a database
                            GetitApplication.setTwitterAccessToken(accessToken);
                        },
                        error -> {
                            LoggingHelper.i(TAG, "Could not get Twitter access token");
                        });
    }

    public void getReverseGeocode(LatLng coordinates) {
        NetworkHelper.getAddressFromCoordinatesSingleObservable(coordinates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reverseGeoResponse -> {
                    if(reverseGeoResponse.isSuccess())
                        presenterListener.onGetAddress(reverseGeoResponse);
                    else
                        LoggingHelper.e(TAG, "getAddressFromCoordinatesSingleObservable unsuccessful : " + reverseGeoResponse.getMessage());
                        },
                        error -> {
                            ReverseGeoResponse tmp = new ReverseGeoResponse();
                            tmp.setSuccess(false);
                            tmp.setMessage("Error: " + error.getMessage());
                            presenterListener.onGetAddress(tmp);
                        });
    }

    public void getInformationFB(SearchRequest searchRequest) {
        if(!searchRequest.getSearchQuery().equalsIgnoreCase("african")) {
            NetworkHelper.getInformationFBSingleObservable(searchRequest)
                    .flatMap(facebookSearchResponse -> {
                        LoggingHelper.d(TAG, "getPlaceFromFB");
                        return placeProcessor.getPlaceFromFBObservable(facebookSearchResponse);
                    })
                    .delay(1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(FBPlaces -> {
                        presenterListener.onGetFacebookPlace(FBPlaces);
                    }, e -> {
                        ArrayList<PlaceInfo> tmp = new ArrayList<>();
                        LoggingHelper.e(TAG, "Error: " + e.getMessage());
                        presenterListener.onGetFacebookPlace(tmp);
                    });
        }else {
            getPlaceFromPredefinedStrings(searchRequest);
        }
    }

    public void getInformationGoogle(SearchRequest searchRequest) {
            GoogleNearByPlacesNetAPI.getNearByPlacesSingle(searchRequest)
                    .flatMap(googleSearchResponse -> {
                        LoggingHelper.d(TAG, "getPlaceFromGoogle");
                        return placeProcessor.getPlaceFromGoogleObservable(googleSearchResponse);
                    })
                    .delay(1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(googlePlaces -> {
                        presenterListener.onGetGooglePlaces(googlePlaces);
                    }, e -> {
                        ArrayList<PlaceInfo> tmp = new ArrayList<>();
                        LoggingHelper.e(TAG, "Error: " + e.getMessage());
                        presenterListener.onGetGooglePlaces(tmp);
                    });
    }

    private void getMultiPageInformationFB(SearchRequest searchRequest) {
        List<FacebookSearchResponse> facebookSearchResponses = new ArrayList<>();

        NetworkHelper.getInformationFBSingleObservable(searchRequest)
                .flatMap(facebookSearchResponse -> {
                    facebookSearchResponses.add(facebookSearchResponse);
                    LoggingHelper.i(TAG, facebookSearchResponse.getPaging().getNext());
                    //return placeProcessor.getPlaceFromFBObservable(facebookSearchResponse);
                    return NetworkHelper.getNextInformationFBSingleObservable(facebookSearchResponse.getPaging().getNext());
                })
                .flatMap(facebookSearchResponse -> {
                    facebookSearchResponses.add(facebookSearchResponse);
                    LoggingHelper.i(TAG, facebookSearchResponse.getPaging().getNext());
                    //return placeProcessor.getPlaceFromFBObservable(facebookSearchResponse);
                    return NetworkHelper.getNextInformationFBSingleObservable(facebookSearchResponse.getPaging().getNext());
                })
                .flatMap(facebookSearchResponse -> {
                    facebookSearchResponses.add(facebookSearchResponse);
                    LoggingHelper.i(TAG, facebookSearchResponse.getPaging().getNext());
                    //return placeProcessor.getPlaceFromFBObservable(facebookSearchResponse);
                    return NetworkHelper.getNextInformationFBSingleObservable(facebookSearchResponse.getPaging().getNext());
                })
                .flatMap(facebookSearchResponse -> {
                    facebookSearchResponses.add(facebookSearchResponse);
                    LoggingHelper.i(TAG, facebookSearchResponse.getPaging().getNext());
                    //return placeProcessor.getPlaceFromFBObservable(facebookSearchResponse);
                    return NetworkHelper.getNextInformationFBSingleObservable(facebookSearchResponse.getPaging().getNext());
                })
                .flatMap(facebookSearchResponse -> {
                    facebookSearchResponses.add(facebookSearchResponse);
                    LoggingHelper.i(TAG, facebookSearchResponse.getPaging().getNext());
                    //return placeProcessor.getPlaceFromFBObservable(facebookSearchResponse);
                    return NetworkHelper.getNextInformationFBSingleObservable(facebookSearchResponse.getPaging().getNext());
                })
                .flatMap(facebookSearchResponse -> {
                    facebookSearchResponses.add(facebookSearchResponse);
                    return Single.just(lookUpKeyWord(searchRequest.getSearchQuery(), facebookSearchResponses));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(FBPlaces -> {
                    presenterListener.onGetFacebookPlace(FBPlaces);
                }, e -> {
                    ArrayList<PlaceInfo> tmp = new ArrayList<>();
                    LoggingHelper.e(TAG, "Error: " + e.getMessage());
                    presenterListener.onGetFacebookPlace(tmp);
                });
    }

    private void getPlaceFromPredefinedStrings(SearchRequest searchRequest){
        Observable.just(foodSearchRequests(searchRequest))
                .flatMap(searchRequests -> Observable.fromIterable(searchRequests)
                        .map(searchRequest1 -> {
                            //LoggingHelper.i(TAG, "Search: " + searchRequest1.getSearchQuery());
                            return NetworkHelper.facebookPlaceSearchDirect(searchRequest1);
                        })
                        .toList()
                        .toObservable()
                )
                .flatMap(facebookSearchResponses -> {
                    LoggingHelper.i(TAG, "Done with Network");
                    return Observable.just(lookUpKeyWord(searchRequest.getSearchQuery(), facebookSearchResponses));
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(FBPlaces -> {
                    LoggingHelper.i(TAG, "Done with KeywordLookup");
                    presenterListener.onGetFacebookPlace(FBPlaces);
                }, e -> {
                    ArrayList<PlaceInfo> tmp = new ArrayList<>();
                    LoggingHelper.e(TAG, "Error: " + e.getMessage());
                    presenterListener.onGetFacebookPlace(tmp);
                });
    }

    private List<SearchRequest> foodSearchRequests(SearchRequest searchRequest){
        List<SearchRequest> searchRequests = new ArrayList<>();
        if(searchRequest.getLocation() != null){
            searchRequests.add(new SearchRequest("african", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("away", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("shisa", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("nyama", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("chesa", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("fire", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("afro", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("braai", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("lounge", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("restaurant", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("cafe", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("shop", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("traditional", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("africa", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("sishebo", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("kota", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("steak", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("momma", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("mamma", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("mama", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("tasty", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("mzansi", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("real", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("store", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));
            searchRequests.add(new SearchRequest("kitchen", searchRequest.getLocation(), searchRequest.getSearchDistenceMeters()));

        }else {
            searchRequests.add(new SearchRequest("african"));
            searchRequests.add(new SearchRequest("away"));
            searchRequests.add(new SearchRequest("shisa"));
            searchRequests.add(new SearchRequest("nyama"));
            searchRequests.add(new SearchRequest("chesa"));
            searchRequests.add(new SearchRequest("fire"));
            searchRequests.add(new SearchRequest("afro"));
            searchRequests.add(new SearchRequest("braai"));
            searchRequests.add(new SearchRequest("lounge"));
            searchRequests.add(new SearchRequest("restaurant"));
            searchRequests.add(new SearchRequest("cafe"));
            searchRequests.add(new SearchRequest("shop"));
            searchRequests.add(new SearchRequest("traditional"));
            searchRequests.add(new SearchRequest("africa"));
            searchRequests.add(new SearchRequest("sishebo"));
            searchRequests.add(new SearchRequest("kota"));
            searchRequests.add(new SearchRequest("steak"));
            searchRequests.add(new SearchRequest("momma"));
            searchRequests.add(new SearchRequest("mamma"));
            searchRequests.add(new SearchRequest("mama"));
            searchRequests.add(new SearchRequest("tasty"));
            searchRequests.add(new SearchRequest("mzansi"));
            searchRequests.add(new SearchRequest("real"));
            searchRequests.add(new SearchRequest("store"));
            searchRequests.add(new SearchRequest("kitchen"));
        }
        return searchRequests;
    }

    private List<PlaceInfo> lookUpKeyWord(String searchQuery, List<FacebookSearchResponse> facebookSearchResponses) {
        List<PlaceInfo> placeInfoList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for(FacebookSearchResponse response : facebookSearchResponses){
            List<PlaceInfo> placeFromFB = PlaceProcessor.getPlaceFromFB(response);
            for(PlaceInfo placeInfo : placeFromFB){
                //if(placeInfo.getDescription() != null && !placeInfoList.contains(placeInfo)){
                if(!titles.contains(placeInfo.getTitle())  /*&& (placeInfo.getDescription() != null && placeInfo.getDescription().contains("Mogodu") || placeInfo.getAbout() != null && placeInfo.getAbout().contains("Mogodu"))*/){
                    titles.add(placeInfo.getTitle());
                    placeInfoList.add(placeInfo);
                }
            }
        }
        return placeInfoList;
    }

    public void getInformationTW(SearchRequest searchRequest) {
        NetworkHelper.getInformationTWSingleObservable(searchRequest)
                .flatMap(twitterSearchResponse -> {
                    LoggingHelper.d(TAG, "getPlaceFromTW");
                    return placeProcessor.getPlaceFromTWObservable(twitterSearchResponse);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(placeInfoList -> {
                    //if(reverseGeoResponse.isSuccess()){
                    //LoggingHelper.d();
                    presenterListener.onGetTweets(placeInfoList);
                    //}
                }, e -> {
                    ArrayList<PlaceInfo> tmp = new ArrayList<>();
                    LoggingHelper.e(TAG, "Error: " + e.getMessage());
                    presenterListener.onGetTweets(tmp);
                    //LoggingHelper.i(TAG, "Twitter Error: " + e.getMessage());
                });
    }

}
