package com.smartalgorithms.getit.Models.Local;

import com.google.gson.Gson;
import com.smartalgorithms.getit.Helpers.GeneralHelper;

import java.util.List;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */

public class ReverseGeoResponse extends NetworkResponse {
    private List<Result> results;

    public static String getAdress(Result result) {
        return result.getFormatted_address();
    }

    public ReverseGeoResponse fromJson(String s) {
        return (ReverseGeoResponse) GeneralHelper.getObjectFromJson(s, ReverseGeoResponse.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public List<Result> getResults() {
        return results;
    }

    public class Result {
        private String formatted_address;
        private List<Component> address_components;

        public String getFormatted_address() {
            return formatted_address;
        }

        public List<Component> getAddress_components() {
            return address_components;
        }

        class Component {
            public String long_name;
            public String short_name;
            public List<String> types;
        }
    }
}
