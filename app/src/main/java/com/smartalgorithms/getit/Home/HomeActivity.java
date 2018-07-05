package com.smartalgorithms.getit.Home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.getit.Adapters.AdapterContract;
import com.smartalgorithms.getit.Adapters.PlaceInfoListAdapter;
import com.smartalgorithms.getit.Drawer.DrawerActivity;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
import com.smartalgorithms.getit.Models.Database.PlaceInfo;
import com.smartalgorithms.getit.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */
//TODO Twitter search - look for @places returned and get more info
//TODO Facebook search - Get food type and search possible place names - Return list of places with the name in description/about
//TODO Get efficient text search library
public class HomeActivity extends DrawerActivity implements HomeContract.UiListener, AdapterContract.UIListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    @BindView(R.id.my_toolbar) Toolbar my_toolbar;
    @BindView(R.id.et_search_term) EditText et_search_term;
    //@BindView(R.id.tv_search) TextView tv_search;
    @BindView(R.id.tv_location) TextView tv_location;
    @BindView(R.id.rv_places) RecyclerView rv_places;
    @BindView(R.id.rlyt_search_area) RelativeLayout rlyt_search_area;
    //@BindView(R.id.llyt_search_button) LinearLayout llyt_search_button;
    //@BindView(R.id.pb_search) ProgressBar pb_search;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_no_place) TextView tv_no_place;
    @BindView(R.id.rlyt_loading) RelativeLayout rlyt_loading;
    @BindView(R.id.rlyt_main_area) RelativeLayout rlyt_main_area;
    @BindView(R.id.lav_loading) LottieAnimationView lav_loading;
    @BindView(R.id.rlyt_default_content) ScrollView rlyt_default_content;
    @BindView(R.id.llyt_content_2) LinearLayout llyt_content_2;
    @BindView(R.id.rlyt_conten_7) RelativeLayout rlyt_conten_7;
    @BindView(R.id.rlyt_loading_location) RelativeLayout rlyt_loading_location;
    @BindView(R.id.lav_loading_location) LottieAnimationView lav_loading_location;
    @BindView(R.id.tv_loading_location) TextView tv_loading_location;
    @BindView(R.id.tv_try_again) TextView tv_try_again;

    private HomePresenter presenter;
    private LatLng coordinates;
    private PlaceInfoListAdapter placeInfoListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_copy);
        setupUI();
        presenter = new HomePresenter(HomeActivity.this, HomeActivity.this);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.smartalgorithms.getit"));

    }


    private void setupUI() {
        ButterKnife.bind(this);
        initialize(my_toolbar);
        setTitle("");
        toggleDefaultContent(true);
        tv_location.setText(GeneralHelper.getString(R.string.text_default_location));
        iv_search.setOnClickListener(clickListener);
        et_search_term.setOnEditorActionListener(onEditorActionListener);
        rlyt_main_area.setOnClickListener(clickListener);
        my_toolbar.setOnClickListener(clickListener);
        llyt_content_2.setOnClickListener(clickListener);
        rlyt_conten_7.setOnClickListener(clickListener);
        tv_try_again.setOnClickListener(clickListener);
        hideKeyboard();
    }

    private View.OnClickListener clickListener = view -> {
        switch (view.getId()) {
            case R.id.iv_search:
                //llyt_search_button.setBackgroundColor(GeneralHelper.getColor(R.color.lightBlue));
                displayAnimation(true);
                hideKeyboard();
                presenter.onButtonClickSearch(et_search_term.getText().toString(), coordinates);
                break;
            case R.id.my_toolbar:
            case R.id.rlyt_main_area:
                hideKeyboard();
                break;
            case R.id.llyt_content_2:
                displayAnimation(true);
                presenter.onButtonClickSearch("any", coordinates);
                break;
            case R.id.rlyt_conten_7:
                displayAnimation(true);
                presenter.onButtonClickSearch("african", coordinates);
                break;
            case R.id.tv_try_again:
                tv_try_again.setVisibility(View.GONE);
                presenter.onButtonClickTryAgain();
                break;
        }
    };

    private TextView.OnEditorActionListener onEditorActionListener = (v, i, e) ->{
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            iv_search.callOnClick();
            return true;
        }
        return false;
    };

    private void toggleDefaultContent(boolean show){
        rlyt_default_content.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (coordinates == null) {
                Single.timer(3000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(t -> displayLocationAnimation(false));
                coordinates = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
                LoggingHelper.i("Broadcast receiver", "lat: " + coordinates.latitude + " lang: " + coordinates.longitude);
                presenter.requestAddress(coordinates);
            } else if (coordinates.longitude != bundle.getDouble("lng") && coordinates.latitude != bundle.getDouble("lat")) {
                Single.timer(3000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(t -> displayLocationAnimation(false));
                coordinates = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
                LoggingHelper.i("Broadcast receiver", "lat: " + coordinates.latitude + " lang: " + coordinates.longitude);
                presenter.requestAddress(coordinates);
            }

        }
    };

    /*private void displayProgressBar(boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        pb_search.setVisibility(show ? View.VISIBLE : View.GONE);
        pb_search.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pb_search.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }*/

    public void displayAnimation(boolean show) {
        if(show){
            rlyt_loading.setVisibility(View.VISIBLE);
            lav_loading.setVisibility(View.VISIBLE);
            lav_loading.setAnimation("loading.json");
            lav_loading.playAnimation();
        }else {
            lav_loading.setVisibility(View.GONE);
            rlyt_loading.setVisibility(View.GONE);
        }

    }

    public void displayLocationAnimation(boolean show) {
        if(show){
            rlyt_loading_location.setVisibility(View.VISIBLE);
            lav_loading_location.setVisibility(View.VISIBLE);
            tv_loading_location.setVisibility(View.VISIBLE);
            lav_loading_location.setAnimation("bouncy_mapmaker.json");
            lav_loading_location.playAnimation();
        }else {
            lav_loading_location.setVisibility(View.GONE);
            rlyt_loading_location.setVisibility(View.GONE);
            tv_loading_location.setVisibility(View.GONE);
        }

    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_search_term.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddressRecieved(boolean success, @Nullable String address) {
        if (!success)
            coordinates = null;
        tv_location.setText(address);
    }

    @Override
    public void onBackPressed() {
        if(rlyt_default_content.getVisibility() == View.VISIBLE)
            presenter.onButtonClickBack();
        else {
            tv_no_place.setVisibility(View.GONE);
            if (placeInfoListAdapter != null)
                placeInfoListAdapter.clear();
            toggleDefaultContent(true);
        }
    }

    @Override
    public void onSearchComplete(ArrayList<PlaceInfo> placeInfoArrayList) {
        LoggingHelper.i(TAG, "onSearchComplete");
        //llyt_search_button.setBackgroundColor(GeneralHelper.getColor(R.color.blue));
        toggleDefaultContent(false);
        displayAnimation(false);
        if (placeInfoArrayList != null) {
            if (placeInfoArrayList != null && placeInfoArrayList.size() > 0) {
                tv_no_place.setVisibility(View.GONE);
                placeInfoListAdapter = new PlaceInfoListAdapter(placeInfoArrayList, coordinates, HomeActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this);
                rv_places.setLayoutManager(mLayoutManager);
                rv_places.setItemAnimator(new DefaultItemAnimator());
                rv_places.setAdapter(placeInfoListAdapter);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rlyt_search_area.setElevation(15);
                }
            } else {
                if (placeInfoListAdapter != null)
                    placeInfoListAdapter.clear();
                tv_no_place.setText(GeneralHelper.getString(R.string.text_no_place_found));
                tv_no_place.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rlyt_search_area.setElevation(0);

                }
            }
        } else {
            if (placeInfoListAdapter != null)
                placeInfoListAdapter.clear();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rlyt_search_area.setElevation(0);
            }
        }
    }

    @Override
    public void showMessage(String title, String message, DialogInterface.OnClickListener ok_listener, DialogInterface.OnClickListener cancel_listener, boolean has_cancel, String positive_text, String negative_text) {
        GeneralHelper.displayDialog(HomeActivity.this, title, message, ok_listener, cancel_listener, has_cancel, positive_text, negative_text);
    }

    @Override
    public void onValidateSearch(String message) {
        et_search_term.setError(message);
    }

    @Override
    public void requestLocation() {
        stopService(GetitApplication.getLocationIntent());
        startService(GetitApplication.getLocationIntent());
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showErrorMessage(String message) {
        Single.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    displayLocationAnimation(false);
                    toggleDefaultContent(false);
                    displayAnimation(false);
                    tv_no_place.setText(message);
                    tv_no_place.setVisibility(View.VISIBLE);
                    tv_try_again.setVisibility(View.VISIBLE);
                });

    }

    @Override
    public void loadDefaultView() {
        toggleDefaultContent(true);
        tv_no_place.setVisibility(View.GONE);
        tv_try_again.setVisibility(View.GONE);
        displayLocationAnimation(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (coordinates == null)
            displayLocationAnimation(true);
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
    public void onTransition(Class<?> toClass) {
        Intent intent = new Intent(HomeActivity.this, toClass);
        startActivity(intent);
    }
}
