package com.smartalgorithms.getit;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2018/01/04.
 * Updated by Ndivhuwo Nthambeleni on 2018/01/04.
 */

public class Constants {
    public final static int REQUEST_TYPE_GET = 1;
    public final static int REQUEST_TYPE_POST = 2;
    public final static int REQUEST_TYPE_PUT = 3;
    public final static int DEFAULT_SEARCH_RADIUS = 10000; //meters
    public final static int PLACE_SOURCE_FB = 1;
    public final static int PLACE_SOURCE_TW = 2;

    public static String getSourceFromInt(int source){
        switch (source){
            case PLACE_SOURCE_FB : return "FB Places";
            case PLACE_SOURCE_TW : return "Twitter Search";
            default: return "No Source";
        }
    }
}

