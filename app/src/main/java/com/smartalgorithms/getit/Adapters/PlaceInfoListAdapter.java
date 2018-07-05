package com.smartalgorithms.getit.Adapters;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.Constants;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.ImageHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Place.PlaceActivity;
import com.smartalgorithms.getit.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class PlaceInfoListAdapter extends RecyclerView.Adapter<PlaceInfoListAdapter.ViewHolder> {
    private static final String TAG = PlaceInfoListAdapter.class.getSimpleName();
    private List<PlaceInfo> placeInfoList;
    private LatLng currentLocation;
    private AdapterContract.UIListener uiListener;
    private Location location;
    private Context context;

    public PlaceInfoListAdapter(List<PlaceInfo> placeInfoList, LatLng currentLocation, AdapterContract.UIListener uiListener) {
        this.currentLocation = currentLocation;
        this.placeInfoList = sortList(placeInfoList);
        this.uiListener = uiListener;
    }

    private List<PlaceInfo> sortList(List<PlaceInfo> placeInfos) {
        List<PlaceInfo> sortedList = new ArrayList<>();
        sortedList.addAll(placeInfos);
        if (currentLocation != null) {
            location = new Location("My current location");
            location.setLatitude(currentLocation.latitude);
            location.setLongitude(currentLocation.longitude);
            //Sort place list by closest.
            Collections.sort(sortedList, (PlaceInfo lhs, PlaceInfo rhs) -> {
                int lhsMetersDifference = 0;
                int rhsMetersDifference = 0;
                if (lhs.getLatitude() == -1 && lhs.getLongitude() == -1) {
                    lhsMetersDifference = 1000000000;
                } else {
                    //LHS meters difference
                    Location lhsFacilityLocation = new Location("LHS Location");
                    lhsFacilityLocation.setLatitude(lhs.getLatitude());
                    lhsFacilityLocation.setLongitude(lhs.getLongitude());
                    lhsMetersDifference = (int) location.distanceTo(lhsFacilityLocation);
                }

                if (rhs.getLatitude() == -1 && rhs.getLongitude() == -1) {
                    rhsMetersDifference = 1000000001;
                } else {
                    //RHS meters difference
                    Location rhsFacilityLocation = new Location("RHS Location");
                    rhsFacilityLocation.setLatitude(rhs.getLatitude());
                    rhsFacilityLocation.setLongitude(rhs.getLongitude());
                    rhsMetersDifference = (int) location.distanceTo(rhsFacilityLocation);
                }
                //String.valueOf();
                //return String.valueOf(lhsMetersDifference).compareTo(String.valueOf(rhsMetersDifference));
                return Integer.compare(lhsMetersDifference, rhsMetersDifference);
            });
            return sortedList;
        }else
            return placeInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_place_info, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.lyt_root.setBackgroundColor(GeneralHelper.getColor(R.color.white));
        final PlaceInfo placeInfo = placeInfoList.get(position);
        final Location placeLocation = new Location("Place location");
        if(placeInfo.getIcon() != "") {
            ImageHelper.picassoLoadImageFromURL(context, holder.iv_place_image, placeInfo.getIcon());
        }

        String title;
        if (placeInfo.getTitle() != null) {
            title = placeInfo.getTitle().length() > 20 ? placeInfo.getTitle().substring(0, 20) + "..." : placeInfo.getTitle();
        } else {
            title = "Place";
        }
        holder.tv_place_title.setText(title);
        if (placeInfo.getCheckins() == 0) {
            holder.tv_place_checkins.setText("-");
        } else {
            holder.tv_place_checkins.setText(placeInfo.getCheckins() + " Checkins");
        }
        holder.tv_place_source.setText(Constants.getSourceFromInt(placeInfo.getSource()));

        if ((placeInfo.getLatitude() == -1 && placeInfo.getLongitude() == -1) || location == null) {
            holder.tv_distance.setText("-");
        } else {
            placeLocation.setLatitude(placeInfo.getLatitude());
            placeLocation.setLongitude(placeInfo.getLongitude());
            int metersDifference = (int) location.distanceTo(placeLocation);
            String distance = metersDifference > 1000 ? metersDifference / 1000.0 + " KM" : metersDifference + " M";
            holder.tv_distance.setText(distance);
        }
        holder.lyt_root.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(placeInfoList.get(position));
            uiListener.onTransition(PlaceActivity.class);
        });
    }

    public void clear() {
        final int size = placeInfoList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                placeInfoList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {
        return placeInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lyt_root)
        LinearLayout lyt_root;
        @BindView(R.id.tv_place_title)
        TextView tv_place_title;
        @BindView(R.id.tv_place_checkins)
        TextView tv_place_checkins;
        @BindView(R.id.tv_place_source)
        TextView tv_place_source;
        @BindView(R.id.tv_distance)
        TextView tv_distance;
        @BindView(R.id.iv_place_image)
        ImageView iv_place_image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ViewHolder.this, itemView);
        }
    }
}
