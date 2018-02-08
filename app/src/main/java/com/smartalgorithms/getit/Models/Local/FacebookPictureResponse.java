package com.smartalgorithms.getit.Models.Local;

import com.google.gson.Gson;
import com.smartalgorithms.getit.Helpers.GeneralHelper;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class FacebookPictureResponse extends NetworkResponse {
    PictureData data;

    public PictureData getData() {
        return data;
    }

    public FacebookPictureResponse fromJson(String s) {
        return (FacebookPictureResponse) GeneralHelper.objectFromJson(s, FacebookPictureResponse.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public class PictureData {
        boolean is_silhouette;
        String url;

        public boolean isIs_silhouette() {
            return is_silhouette;
        }

        public String getUrl() {
            return url;
        }
    }

}
