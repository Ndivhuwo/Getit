package com.smartalgorithms.getit.Adapters;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.Constants;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.PlaceSelection.PlaceSelectionActivity;
import com.smartalgorithms.getit.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class PlaceInfoAdapter extends RecyclerView.Adapter<PlaceInfoAdapter.ViewHolder>{
    private static final String TAG = PlaceInfoAdapter.class.getSimpleName();
    private List<PlaceInfo> placeInfoList;
    private LatLng currentLocation;
    private AdapterContract.UIListener uiListener;

    public PlaceInfoAdapter(List<PlaceInfo> placeInfoList, LatLng currentLocation, AdapterContract.UIListener uiListener) {
        this.placeInfoList = placeInfoList;
        this.currentLocation = currentLocation;
        this.uiListener = uiListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_place_info, parent, false);
        return new ViewHolder(itemView, currentLocation);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PlaceInfo placeInfo = placeInfoList.get(position);
        final Location placeLocation = new Location("Place location");
        holder.tv_place_title.setText(placeInfo.getTitle());
        if(placeInfo.getCheckins() == 0){
            holder.tv_place_checkins.setText("Unkown Checkins");
        }else {
            holder.tv_place_checkins.setText(placeInfo.getCheckins() + " Checkins");
        }
        holder.tv_place_source.setText(Constants.getSourceFromInt(placeInfo.getSource()));

        if(placeInfo.getLatitude() == -1 && placeInfo.getLongitude() == -1){
            holder.tv_distance.setText("Unknown KM");
        }else {
            placeLocation.setLatitude(placeInfo.getLatitude());
            placeLocation.setLongitude(placeInfo.getLongitude());
            int metersDifference = (int) holder.location.distanceTo(placeLocation);
            String distance = metersDifference > 1000 ? metersDifference/1000.0 + " KM" : metersDifference + " M";
            holder.tv_distance.setText(distance);
            holder.lyt_root.setOnClickListener(v -> {
                uiListener.onTransition(PlaceSelectionActivity.class);
            });

        }
    }

    @Override
    public int getItemCount() {
        return placeInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lyt_root;
        TextView tv_place_title;
        TextView tv_place_checkins;
        TextView tv_place_source;
        TextView tv_distance;
        ImageView iv_place_image;
        Location location;

        public ViewHolder(View itemView, LatLng locationCoordinates) {
            super(itemView);
            location = new Location("My current location");
            location.setLatitude(locationCoordinates.latitude);
            location.setLongitude(locationCoordinates.longitude);
            //Sort facilty list by closest.
            Collections.sort(placeInfoList, (PlaceInfo lhs, PlaceInfo rhs) -> {
                int lhsMetersDifference = 0;
                int rhsMetersDifference = 0;
                if(lhs.getLatitude() == -1 && lhs.getLongitude() == -1){
                    lhsMetersDifference = 1000000000;
                } else {
                    //LHS meters difference
                    Location lhsFacilityLocation = new Location("LHS Location");
                    lhsFacilityLocation.setLatitude(lhs.getLatitude());
                    lhsFacilityLocation.setLongitude(lhs.getLongitude());
                    lhsMetersDifference = (int) location.distanceTo(lhsFacilityLocation);
                }

                if(rhs.getLatitude() == -1 && rhs.getLongitude() == -1){
                    rhsMetersDifference = 1000000001;
                }
                else {
                    //RHS meters difference
                    Location rhsFacilityLocation = new Location("RHS Location");
                    rhsFacilityLocation.setLatitude(rhs.getLatitude());
                    rhsFacilityLocation.setLongitude(rhs.getLongitude());
                    rhsMetersDifference = (int) location.distanceTo(rhsFacilityLocation);
                }
                    //String.valueOf();
                return String.valueOf(lhsMetersDifference).compareTo(String.valueOf(rhsMetersDifference));
            });
            lyt_root = itemView.findViewById(R.id.lyt_root);
            tv_place_title = itemView.findViewById(R.id.tv_place_title);
            tv_place_checkins = itemView.findViewById(R.id.tv_place_checkins);
            tv_place_source = itemView.findViewById(R.id.tv_place_source);
            tv_distance = itemView.findViewById(R.id.tv_distance);
            iv_place_image = itemView.findViewById(R.id.iv_place_image);
        }
    }
}
