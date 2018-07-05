package com.smartalgorithms.getit.Place;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.ReverseGeoResponse;
import com.smartalgorithms.getit.R;

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
    private Context context;
    private PlaceImageAdapter placeImageAdapter;

    public PlacePresenter(Context context, PlaceContract.UIListener uiListener, PlaceInfo placeInfo) {
        this.context = context;
        this.uiListener = uiListener;
        this.placeInfo = placeInfo;
        placeInteractor = new PlaceInteractor(PlacePresenter.this);
        placeInteractor.initialize(this.placeInfo);
    }

    @Override
    public void getUIStrings() {
        String title = placeInfo.getTitle().length() > 20 ? placeInfo.getTitle().substring(0, 20) + "..." : placeInfo.getTitle();
        StringBuilder info = new StringBuilder();
        info.append("Name: " + placeInfo.getTitle() + "\n");
        if (placeInfo.getAbout() != null) {
            info.append("About: " + placeInfo.getAbout() + "\n");
        }
        if (placeInfo.getDescription() != null) {
            info.append("Description: " + placeInfo.getDescription());
        }
        String checkins = placeInfo.getCheckins() == 0 ? "Unknown Checkins" : placeInfo.getCheckins() + " Checkins";
        String link_phone = placeInfo.getPhone() == null ? placeInfo.getLink() : placeInfo.getPhone();

        uiListener.updateUI(title, info.toString(), link_phone, checkins);
    }

    @Override
    public void requestAddress() {
        if (!GeneralHelper.isInternetAvailable()) {
            GeneralHelper.displayToast(GeneralHelper.getString(R.string.error_body_internet_connection_required));
        } else {
            if (placeInfo.getLongitude() != 0 && placeInfo.getLatitude() != 0) {
                LatLng coordinates = new LatLng(placeInfo.getLatitude(), placeInfo.getLongitude());
                placeInteractor.getReverseGeocode(coordinates);
            }

        }
    }

    @Override
    public void onGetAddress(ReverseGeoResponse reverseGeoResponse) {
        if (reverseGeoResponse.isSuccess()) {
            uiListener.onAddressRecieved(reverseGeoResponse.getResults().get(0).getFormatted_address());
        } else {
            uiListener.onAddressRecieved(reverseGeoResponse.getMessage());
        }
    }

    @Override
    public void onGetImageUrlsComplete(List<String> urls) {
        if (urls != null) {
            placeImageAdapter = new PlaceImageAdapter(context, urls);
            uiListener.onAdapterCreated(placeImageAdapter);
        } else {
            LoggingHelper.e(TAG, "Error getting picture URLs from the ids");
        }
    }

    public void goToPlace() {
        DialogInterface.OnClickListener ok_listener = (dialog, which) -> {
            //String startingAddress = coordinates.latitude + "," + coordinates.longitude;
            String destinationAddress = placeInfo.getLatitude() + "," + placeInfo.getLongitude();
            Uri uri = Uri.parse("google.navigation:q=" + destinationAddress);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                GeneralHelper.displayToast(GeneralHelper.getString(R.string.text_no_maps_app));
            }

        };
        if (placeInfo.getLongitude() != 0 && placeInfo.getLatitude() != 0) {
            uiListener.showMessage(GeneralHelper.getString(R.string.title_navigation),
                    GeneralHelper.getString(R.string.sentence_navigation_message),
                    ok_listener, null, true, "YES", "NO");
        } else
            uiListener.showMessage(GeneralHelper.getString(R.string.title_error), GeneralHelper.getString(R.string.sentence_no_location), null, null, false, "OK", "");
    }
}
