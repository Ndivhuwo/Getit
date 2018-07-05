package com.smartalgorithms.getit.Drawer;

import android.app.Activity;
import android.os.CountDownTimer;

import com.smartalgorithms.getit.Adapters.AdapterContract;
import com.smartalgorithms.getit.Adapters.DrawerAdaper;
import com.smartalgorithms.getit.GetitApplication;
import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
import com.smartalgorithms.getit.Helpers.SharedPreferencesHelper;
import com.smartalgorithms.getit.R;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class DrawerPresenter implements AdapterContract.DrawerListener, DrawerInteractor.PresenterListener {
    private static final String TAG = DrawerPresenter.class.getSimpleName();
    private Activity activity;
    private UIListener uiListener;
    private DrawerAdaper drawerAdaper;
    private String message;
    private int position;
    CountDownTimer timer = new CountDownTimer(700, 200) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished == 300) {
                drawerAdaper.resetSaveButton();
            }
        }

        @Override
        public void onFinish() {
            uiListener.onCloseDrawer(position, message);
        }
    };
    private DrawerInteractor drawerInteractor;

    public DrawerPresenter(Activity activity, UIListener uiListener) {
        this.activity = activity;
        this.uiListener = uiListener;
        this.drawerInteractor = new DrawerInteractor(DrawerPresenter.this);
        initialize();
    }

    private void initialize() {
        getSharedPreferences();
    }

    private void getSharedPreferences() {
        Single.just(SharedPreferencesHelper.getLocationRadius(activity, R.string.text_shared_pref_location_radius))
                .flatMap(radius -> {
                    GetitApplication.setSearchDistance(radius);
                    return Single.just(SharedPreferencesHelper.getUseCurrentLocation(activity, R.string.text_shared_pref_use_current_location));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(useCurrentLocation -> {
                    GetitApplication.setUseCurrentLocation(useCurrentLocation);
                    drawerAdaper = new DrawerAdaper(activity, GeneralHelper.getStringArray(R.array.drawer_items), DrawerPresenter.this, GetitApplication.getSearchDistance(), useCurrentLocation);
                    uiListener.onAdapterLoaded(drawerAdaper);
                }, error -> LoggingHelper.e(TAG, error.getMessage()));
    }

    @Override
    public void onSaveLocationRadius(int locationRadius) {
        Completable.fromAction(() -> SharedPreferencesHelper.setLocationRadius(activity, R.string.text_shared_pref_location_radius, locationRadius))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            LoggingHelper.d(TAG, "setLocationRadius Success");
                            message = "setLocationRadius Success";
                            position = 1;
                            GetitApplication.setSearchDistance(locationRadius);
                            timer.start();
                        },
                        error -> {
                            LoggingHelper.d(TAG, "setLocationRadius Failed: " + error.getMessage());
                            message = "setLocationRadius Failed: " + error.getMessage();
                            position = 1;
                            timer.start();
                        });
    }

    @Override
    public void onUseCurrentLocationChange(boolean isChecked) {
        Completable.fromAction(() -> SharedPreferencesHelper.setUseCurrentLocation(activity, R.string.text_shared_pref_use_current_location, isChecked))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            LoggingHelper.d(TAG, "setUseCurrentLocation Success");
                            message = "setUseCurrentLocation Success";
                            position = 2;
                            GetitApplication.setUseCurrentLocation(isChecked);

                            uiListener.requestLocation(isChecked);
                            timer.start();
                        },
                        error -> {
                            LoggingHelper.d(TAG, "setUseCurrentLocation Failed: " + error.getMessage());
                            message = "setUseCurrentLocation Failed: " + error.getMessage();
                            position = 2;
                            timer.start();
                        });
    }

    public interface UIListener {
        void onAdapterLoaded(DrawerAdaper drawerAdaper);

        void onCloseDrawer(int position, String message);

        void requestLocation(boolean isChecked);
    }
}
