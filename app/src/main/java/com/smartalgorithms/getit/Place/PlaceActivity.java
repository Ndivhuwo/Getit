package com.smartalgorithms.getit.Place;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartalgorithms.getit.Constants;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.Logger;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.Models.Local.PhotoData;
import com.smartalgorithms.getit.R;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;
import com.veinhorn.scrollgalleryview.loader.picasso.PicassoImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class PlaceActivity extends AppCompatActivity implements PlaceContract.UIListener{
    private static final String TAG = PlaceActivity.class.getSimpleName();
    @BindView(R.id.tv_place_title) TextView tv_place_title;
    @BindView(R.id.etv_description) ExpandableTextView etv_description;
    @BindView(R.id.iv_desc_toogle) ImageView iv_desc_toogle;
    @BindView(R.id.llyt_place_details) LinearLayout llyt_place_details;
    @BindView(R.id.tv_address) TextView tv_address;
    @BindView(R.id.tv_place_link) TextView tv_place_link;
    @BindView(R.id.tv_place_checkins) TextView tv_place_checkins;
    @BindView(R.id.iv_directions) ImageView iv_directions;
    @BindView(R.id.llyt_location) LinearLayout llyt_location;
    /*
    @BindView(R.id.tv_service_title) TextView tv_service_title;
    @BindView(R.id.rv_menu_items) RecyclerView rv_menu_items;*/
    @BindView(R.id.sgv_image_gallery) ScrollGalleryView sgv_image_gallery;

    private PlacePresenter placePresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        setupUI();
        //EventBus.getDefault().register(this);
    }

    private void setupUI() {
        ButterKnife.bind(this);
        etv_description.setInterpolator(new OvershootInterpolator());
        llyt_place_details.setOnClickListener(clickListener);
        tv_place_link.setOnClickListener(clickListener);
        iv_directions.setOnClickListener(clickListener);
    }

    @Override
    public void prepareGallery(List<String> imageLinks) {
        Logger.i(TAG, "Images: " + imageLinks.toString());
        List<MediaInfo> infos = new ArrayList<>(imageLinks.size());
        for (String photo : imageLinks) {
                infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(photo)));
        }

        sgv_image_gallery
                .setThumbnailSize(50)
                .hideThumbnails(true)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager());
        if(infos.size() > 0){
            sgv_image_gallery.addMedia(infos);
        }else {
            sgv_image_gallery.addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.ic_place_icon))); //Can also Pass a bitmap
        }
    }

    @Override
    public void updateUI(String title, String info, String link_phone, String checkins) {
        tv_place_title.setText(title);
        etv_description.setText(info);
        tv_place_link.setText(link_phone);
        tv_place_checkins.setText(checkins);
    }

    View.OnClickListener clickListener = view -> {
      switch (view.getId()){
          case R.id.llyt_place_details:
              iv_desc_toogle.setImageDrawable(etv_description.isExpanded() ? GeneralHelper.getDrawable(R.drawable.ic_expand_more_black_36dp): GeneralHelper.getDrawable(R.drawable.ic_expand_less_black_36dp));
              etv_description.toggle();
              break;
          case R.id.tv_place_link:
              //TODO open link on browser if available (Or open phone dialer)
              break;
          case R.id.iv_directions:
              //TODO open google maps to navigate
              break;
      }
    };

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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void recieveImageList(PlaceInfo placeInfo){
        placePresenter = new PlacePresenter(PlaceActivity.this, placeInfo);
    }

    @Override
    public void onAddressRecieved(String formatted_address) {
        tv_address.setText(formatted_address);
    }
}
