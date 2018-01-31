package com.smartalgorithms.getit.Models.Local;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2018/01/04.
 * Updated by Ndivhuwo Nthambeleni on 2018/01/04.
 */

public class NetworkResponse {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
