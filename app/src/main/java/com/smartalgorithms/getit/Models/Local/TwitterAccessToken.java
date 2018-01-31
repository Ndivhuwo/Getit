package com.smartalgorithms.getit.Models.Local;

import com.google.gson.Gson;
import com.smartalgorithms.getit.Helpers.GeneralHelper;

/**
 * Copyright (c) 2017 Smart Algorithms (PTY) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2018/01/21.
 * Updated by Ndivhuwo Nthambeleni on 2018/01/21.
 */

public class TwitterAccessToken extends NetworkResponse{
    String token_type;
    String access_token;

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public TwitterAccessToken fromJson(String s) {
        return (TwitterAccessToken) GeneralHelper.objectFromJson(s, TwitterAccessToken.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
