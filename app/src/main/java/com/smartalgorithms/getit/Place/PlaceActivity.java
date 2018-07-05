package com.smartalgorithms.getit.Place;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.rd.PageIndicatorView;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class PlaceActivity extends AppCompatActivity implements PlaceContract.UIListener {
    private static final String TAG = PlaceActivity.class.getSimpleName();
    @BindView(R.id.tv_place_title)
    TextView tv_place_title;
    @BindView(R.id.etv_description)
    ExpandableTextView etv_description;
    @BindView(R.id.iv_desc_toogle)
    ImageView iv_desc_toogle;
    @BindView(R.id.llyt_place_details)
    LinearLayout llyt_place_details;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_place_link)
    TextView tv_place_link;
    @BindView(R.id.tv_place_checkins)
    TextView tv_place_checkins;
    @BindView(R.id.iv_directions)
    ImageView iv_directions;
    @BindView(R.id.llyt_location)
    LinearLayout llyt_location;
    @BindView(R.id.piv_image_indicator)
    PageIndicatorView piv_image_indicator;
    @BindView(R.id.vp_image_swipe)
    ViewPager vp_image_swipe;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private PlacePresenter placePresenter;
    View.OnClickListener clickListener = view -> {
        switch (view.getId()) {
            case R.id.llyt_place_details:
                iv_desc_toogle.setImageDrawable(etv_description.isExpanded() ? GeneralHelper.getDrawable(R.drawable.expand_button) : GeneralHelper.getDrawable(R.drawable.dexpand_button));
                etv_description.toggle();
                break;
            case R.id.tv_place_link:
                //TODO open link on browser if available (Or open phone dialer)
                break;
            case R.id.iv_directions:
                placePresenter.goToPlace();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    };
    private LatLng coordinates;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            coordinates = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
            LoggingHelper.i("Broadcast receiver", "lat: " + coordinates.latitude + " lang: " + coordinates.longitude);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        setupUI();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.smartalgorithms.getit"));
    }

    private void setupUI() {
        ButterKnife.bind(this);
        etv_description.setInterpolator(new OvershootInterpolator());
        llyt_place_details.setOnClickListener(clickListener);
        tv_place_link.setOnClickListener(clickListener);
        iv_directions.setOnClickListener(clickListener);
        iv_back.setOnClickListener(clickListener);
        vp_image_swipe.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                piv_image_indicator.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });
    }

    @Override
    public void onAdapterCreated(PlaceImageAdapter placeImageAdapter) {
        LoggingHelper.i(TAG, "Images: " + placeImageAdapter.getImages().toString());

        piv_image_indicator.setCount(placeImageAdapter.getImages().size());
        piv_image_indicator.setSelected(0);
        vp_image_swipe.setAdapter(placeImageAdapter);
    }

    @Override
    public void updateUI(String title, String info, String link_phone, String checkins) {
        tv_place_title.setText(title);
        etv_description.setText(info);
        tv_place_link.setText(link_phone);
        tv_place_checkins.setText(checkins);
    }

    @Override
    public void showMessage(String title, String message, DialogInterface.OnClickListener ok_listener, DialogInterface.OnClickListener cancel_listener, boolean has_cancel, String positive_text, String negative_text) {
        GeneralHelper.displayDialog(PlaceActivity.this, title, message, ok_listener, cancel_listener, has_cancel, positive_text, negative_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(GetitApplication.getLocationIntent());
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.smartalgorithms.getit"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(GetitApplication.getLocationIntent());
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        stopService(GetitApplication.getLocationIntent());
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        PlaceActivity.this.finish();
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void recieveImageList(PlaceInfo placeInfo) {
        placePresenter = new PlacePresenter(PlaceActivity.this, PlaceActivity.this, placeInfo);
    }

    @Override
    public void onAddressRecieved(String formatted_address) {
        tv_address.setText(formatted_address);
    }
}
