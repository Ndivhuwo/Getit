package com.smartalgorithms.getit.Models.Local;

import com.google.android.gms.maps.model.LatLng;

/**
 * Copyright (c) 2017 Smart Algorithms (PTY) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2018/01/09.
 * Updated by Ndivhuwo Nthambeleni on 2018/01/09.
 */

public class SearchRequest {
    String searchQuery;
    LatLng location;
    int searchDistenceMeters;

    public SearchRequest(String searchTerm) {
        this.searchQuery = searchTerm;
    }

    public SearchRequest(String searchTerm, LatLng locationJson) {
        this.searchQuery = searchTerm;
        this.location = locationJson;
    }

    public SearchRequest(String searchQuery, int searchDistenceMeters) {
        this.searchQuery = searchQuery;
        this.searchDistenceMeters = searchDistenceMeters;
    }

    public SearchRequest(String searchQuery, LatLng location, int searchDistenceMeters) {
        this.searchQuery = searchQuery;
        this.location = location;
        this.searchDistenceMeters = searchDistenceMeters;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public LatLng getLocation() {
        return location;
    }

    public int getSearchDistenceMeters() {
        return searchDistenceMeters;
    }

    public int getSearchDistenceKM(){
        return searchDistenceMeters/1000;
    }
}
