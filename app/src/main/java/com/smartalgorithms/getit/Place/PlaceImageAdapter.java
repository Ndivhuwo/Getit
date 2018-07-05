package com.smartalgorithms.getit.Place;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.smartalgorithms.getit.Helpers.GeneralHelper;
import com.smartalgorithms.getit.Helpers.ImageHelper;
import com.smartalgorithms.getit.Helpers.LoggingHelper;
import com.smartalgorithms.getit.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2018/02/26.
 * Updated by Ndivhuwo Nthambeleni on 2018/02/26.
 */

public class PlaceImageAdapter extends PagerAdapter {
    private static final String TAG = PlaceImageAdapter.class.getSimpleName();
    @BindView(R.id.iv_image)
    ImageView iv_image;
    private Context context;
    private List<String> images;
    private LayoutInflater layoutInflater;

    public PlaceImageAdapter(Context context, List<String> images) {
        this.context = context;
        if (images.size() > 0)
            this.images = images;
        else {
            this.images = new ArrayList<>();
            this.images.add(String.valueOf(R.drawable.ic_place_icon));
        }
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LoggingHelper.d(TAG, "instantiateItem");

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = layoutInflater.inflate(R.layout.item_place_image, container, false);
        ButterKnife.bind(PlaceImageAdapter.this, viewLayout);

        if (GeneralHelper.isURL(images.get(position)))
            ImageHelper.picassoLoadImageFromURL(context, iv_image, images.get(position));
        else
            ImageHelper.picassoLoadImageFromDrawable(context, iv_image, Integer.parseInt(images.get(position)));

        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public List<String> getImages() {
        return images;
    }
}
