package com.smartalgorithms.getit.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
import com.smartalgorithms.getit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class DrawerAdaper extends ArrayAdapter<String> {
    private static final String TAG = DrawerAdaper.class.getSimpleName();

    private AdapterContract.DrawerListener drawerListener;
    private int distanceRadius;
    private boolean useCurrentLocation;
    private ViewHolder viewHolder;

    public DrawerAdaper(@NonNull Context context, @NonNull String[] resource, AdapterContract.DrawerListener drawerListener, int distanceRadius, boolean useCurrentLocation) {
        super(context, 0, resource);
        this.drawerListener = drawerListener;
        this.distanceRadius = distanceRadius;
        this.useCurrentLocation = useCurrentLocation;
    }

    public void setDistanceRadius(int distanceRadius) {
        this.distanceRadius = distanceRadius;
    }

    public void setUseCurrentLocation(boolean useCurrentLocation) {
        this.useCurrentLocation = useCurrentLocation;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (position == 0) {
            LoggingHelper.d(TAG, "Position 1");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_drawer_header, parent, false);

        } else {
            if (convertView == null || convertView.getTag() == null) {
                LoggingHelper.i(TAG, "convertView is null");
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_drawer, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                LoggingHelper.i(TAG, "convertView is not null");
                viewHolder = (ViewHolder) convertView.getTag();
            }

            View.OnClickListener onClickListener = v -> {
                switch (v.getId()) {
                    case R.id.iv_save:
                        this.distanceRadius = Integer.parseInt(viewHolder.et_search_radius.getText().toString());
                        notifyDataSetChanged();
                        viewHolder.iv_save.setImageDrawable(GeneralHelper.getDrawable(R.drawable.img_save_light_blue));
                        drawerListener.onSaveLocationRadius(Integer.parseInt(viewHolder.et_search_radius.getText().toString()));
                        break;
                }
            };

            //viewHolder.sw_use_location.

            Switch.OnCheckedChangeListener onCheckedChangeListener = (btn, isChecked) -> {
                if (btn.getId() == R.id.sw_use_location) {
                    this.useCurrentLocation = isChecked;
                    notifyDataSetChanged();
                    drawerListener.onUseCurrentLocationChange(isChecked);
                }
            };

            if (position == 1) {
                LoggingHelper.d(TAG, "Position 2");
                viewHolder.llyt_item_1.setVisibility(View.VISIBLE);
                viewHolder.llyt_item.setVisibility(View.INVISIBLE);
                viewHolder.sw_use_location.setOnCheckedChangeListener(onCheckedChangeListener);

            } else if (position == 2) {
                LoggingHelper.i(TAG, "Position 3");
                viewHolder.llyt_item.setVisibility(View.VISIBLE);
                viewHolder.llyt_item_1.setVisibility(View.INVISIBLE);
                viewHolder.iv_save.setOnClickListener(onClickListener);

            }
        }

        return convertView;
    }

    public void resetSaveButton() {
        viewHolder.iv_save.setImageDrawable(GeneralHelper.getDrawable(R.drawable.img_save_blue));
    }

    public class ViewHolder {
        @BindView(R.id.et_search_radius)
        EditText et_search_radius;
        @BindView(R.id.iv_save)
        ImageView iv_save;
        @BindView(R.id.sw_use_location)
        Switch sw_use_location;
        @BindView(R.id.llyt_item)
        LinearLayout llyt_item;
        @BindView(R.id.llyt_item_1)
        LinearLayout llyt_item_1;

        public ViewHolder(View convertView) {
            ButterKnife.bind(ViewHolder.this, convertView);
            //et_search_radius
            sw_use_location.setChecked(useCurrentLocation);
            et_search_radius.setText(distanceRadius + "");
        }
    }
}
