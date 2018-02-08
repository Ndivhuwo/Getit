package com.smartalgorithms.getit.PlaceUtility;

import com.smartalgorithms.getit.Constants;
import com.smartalgorithms.getit.Helpers.NetworkHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.FacebookSearchResponse;
import com.smartalgorithms.getit.Models.Local.PhotoData;
import com.smartalgorithms.getit.Models.Local.TwitterSearchResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class PlaceProcessor {

    private List<PlaceInfo> getPlaceFromFB(FacebookSearchResponse facebookSearchResponse){
        ArrayList<PlaceInfo> placelist = new ArrayList<>();
        for (FacebookSearchResponse.FacebookData data : facebookSearchResponse.getData()){
            PlaceInfo placeInfo = new PlaceInfo();
            placeInfo.setTitle(data.getName());
            placeInfo.setDescription(data.getDescription());
            placeInfo.setAbout(data.getAbout());
            placeInfo.getImageLinks().add(new PhotoData(data.getPicture().getData().getUrl(), Constants.PHOTO_DATA_TYPE_URL));
            placeInfo.setLink(data.getLink());
            placeInfo.setLatitude(data.getLocation().getLatitude());
            placeInfo.setLongitude(data.getLocation().getLongitude());
            placeInfo.setPhone(data.getPhone());
            placeInfo.setCheckins(data.getCheckins());
            placeInfo.setSource(Constants.PLACE_SOURCE_FB);

            if(data.getPhotos() != null){
                for(PhotoData photoData : data.getPhotos().getData()){
                    photoData.setType(Constants.PHOTO_DATA_TYPE_ID);
                    //NetworkHelper.facebookGetPicture(photoData.getId()).getData().getUrl()
                    placeInfo.getImageLinks().add(photoData);
                }
            }
            placelist.add(placeInfo);
        }
        return placelist;
    }

    private List<PlaceInfo> getPlaceFromTW(TwitterSearchResponse twitterSearchResponse){
        ArrayList<PlaceInfo> placeList = new ArrayList<>();
        for (TwitterSearchResponse.Tweet tweet: twitterSearchResponse.getStatuses()){
            PlaceInfo placeInfo = new PlaceInfo();
            StringBuilder title = new StringBuilder("");
            for (TwitterSearchResponse.Tweet.TWEntity.HashTag hashTag : tweet.getEntities().getHashtags()){
                title.append(hashTag.getText() + ", ");
            }
            if(title.toString() == ""){
                placeInfo.setTitle(tweet.getText());
            }else {
                placeInfo.setDescription(tweet.getText());
            }
            if(tweet.getGeo() != null) {
                placeInfo.setLatitude(tweet.getGeo().getCoordinates()[0]);
                placeInfo.setLongitude(tweet.getGeo().getCoordinates()[1]);
            }else {
                placeInfo.setLatitude(-1);
                placeInfo.setLongitude(-1);
            }

            placeInfo.setCheckins(0);
            placeInfo.setSource(Constants.PLACE_SOURCE_TW);
            placeList.add(placeInfo);
        }
        return placeList;
    }

    public Single<List<PlaceInfo>> getPlaceFromFBObservable(FacebookSearchResponse facebookSearchResponse){
        return Single.defer(() -> Single.just(getPlaceFromFB(facebookSearchResponse)));
    }

    public Single<List<PlaceInfo>> getPlaceFromTWObservable(TwitterSearchResponse twitterSearchResponse){
        return Single.defer(() -> Single.just(getPlaceFromTW(twitterSearchResponse)));
    }
}