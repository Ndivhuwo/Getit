package com.smartalgorithms.getit.Drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;
import com.smartalgorithms.getit.Adapters.DrawerAdaper;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class DrawerActivity extends AppCompatActivity implements DrawerPresenter.UIListener {
    private static final String TAG = DrawerActivity.class.getSimpleName();
    @BindView(R.id.dl_root_layout) DrawerLayout dl_root_layout;
    @BindView(R.id.lv_right_drawer) ListView lv_right_drawer;
    @BindView(R.id.iv_navigation_drawer) ImageView iv_navigation_drawer;
    @BindView(R.id.lav_navigation_drawer) LottieAnimationView lav_navigation_drawer;

    private Toolbar toolbar;
    private DrawerPresenter presenter;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public void initialize(Toolbar toolbar) {
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        setupUI();
        presenter = new DrawerPresenter(DrawerActivity.this, DrawerActivity.this);
    }

    private void setupUI() {
        ButterKnife.bind(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(DrawerActivity.this, dl_root_layout, toolbar, R.string.text_drawer_opened, R.string.text_drawer_closed){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        //actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        dl_root_layout.post(() -> actionBarDrawerToggle.syncState());
        dl_root_layout.addDrawerListener(actionBarDrawerToggle);
        //toolbar.setNavigationOnClickListener(onClickListener);
        //iv_navigation_drawer.setOnClickListener(onClickListener);
        //toolbar.setNavigationIcon(null);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()) {
            case R.id.iv_navigation_drawer:
                toogleDrawer();
                break;
        }
    };

    public void displayDrawerAnimation(boolean show) {
        if (show) {
            iv_navigation_drawer.setVisibility(View.GONE);
            lav_navigation_drawer.setVisibility(View.VISIBLE);
            lav_navigation_drawer.setAnimation("hamburger.json");
            lav_navigation_drawer.playAnimation();
        } else {
            iv_navigation_drawer.setVisibility(View.VISIBLE);
            lav_navigation_drawer.setVisibility(View.GONE);
        }
    }

    private void toogleDrawer() {
        if (dl_root_layout.isDrawerOpen(Gravity.LEFT)) {
            dl_root_layout.closeDrawer(Gravity.LEFT);
        } else {
            dl_root_layout.openDrawer(Gravity.LEFT);

        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            actionBarDrawerToggle.syncState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAdapterLoaded(DrawerAdaper drawerAdaper) {
        lv_right_drawer.setAdapter(drawerAdaper);
    }

    @Override
    public void onCloseDrawer(int position, String message) {
        GeneralHelper.displayToast(message);
        lv_right_drawer.setItemChecked(position, true);
        dl_root_layout.closeDrawer(lv_right_drawer);
    }

    @Override
    public void requestLocation(boolean isChecked) {
        if(isChecked) {
            stopService(GetitApplication.getLocationIntent());
            startService(GetitApplication.getLocationIntent());
        }
        else {
            stopService(GetitApplication.getLocationIntent());
        }
    }
}
