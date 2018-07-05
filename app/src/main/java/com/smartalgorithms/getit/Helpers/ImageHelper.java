package com.smartalgorithms.getit.Helpers;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.smartalgorithms.getit.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2018/02/26.
 * Updated by Ndivhuwo Nthambeleni on 2018/02/26.
 */


public class ImageHelper {
    private static final String TAG = ImageHelper.class.getSimpleName();

    public static void picassoLoadImageFromURL(Context context, ImageView imageView, String url) {
        LoggingHelper.d(TAG, "picassoLoadImageFromURL");

        Picasso.with(context)
                .load(url)
                .placeholder(GeneralHelper.getDrawable(R.drawable.img_placeholder))
                .error(GeneralHelper.getDrawable(R.drawable.img_error))
                .fit()
                .centerInside()
                .into(imageView);
    }

    public static void picassoLoadImageFromFile(Context context, ImageView imageView, File file) {
        LoggingHelper.d(TAG, "picassoLoadImageFromFile");

        Picasso.with(context)
                .load(file)
                .placeholder(GeneralHelper.getDrawable(R.drawable.img_placeholder))
                .error(GeneralHelper.getDrawable(R.drawable.img_error))
                .fit()
                .centerInside()
                .into(imageView);
    }

    public static void picassoLoadImageFromDrawable(Context context, ImageView imageView, int resource) {
        LoggingHelper.d(TAG, "picassoLoadImageFromFile");

        Picasso.with(context)
                .load(resource)
                .placeholder(GeneralHelper.getDrawable(R.drawable.img_placeholder))
                .error(GeneralHelper.getDrawable(R.drawable.img_error))
                .fit()
                .centerInside()
                .into(imageView);
    }

    public static String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }
}
