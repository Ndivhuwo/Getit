package com.smartalgorithms.getit.Models.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Copyright (c) 2017 Smart Algorithms (Pty) Ltd. All rights reserved
 * Contact info@smartalgorithms.co.za
 * Created by Ndivhuwo Nthambeleni on 2018/01/27.
 * Updated by Ndivhuwo Nthambeleni on 2018/01/27.
 */
@Entity
public class PlaceInfo {
    @Id
    private Long placeId;
    private String title;
    private String description;
    private String phone;
    private String link;
    private String imageLink;
    private String imagePath;
    private int checkins;
    private double latitude;
    private double longitude;
    private String address;
    private int uploaded;
    private int source;

    @Generated(hash = 33530299)
    public PlaceInfo(Long placeId, String title, String description, String phone,
            String link, String imageLink, String imagePath, int checkins,
            double latitude, double longitude, String address, int uploaded,
            int source) {
        this.placeId = placeId;
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.link = link;
        this.imageLink = imageLink;
        this.imagePath = imagePath;
        this.checkins = checkins;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.uploaded = uploaded;
        this.source = source;
    }
    @Generated(hash = 2046624665)
    public PlaceInfo() {
    }

    public Long getPlaceId() {
        return this.placeId;
    }
    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getLink() {
        return this.link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getImageLink() {
        return this.imageLink;
    }
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return this.longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getUploaded() {
        return this.uploaded;
    }
    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }
    public int getSource() {
        return this.source;
    }
    public void setSource(int source) {
        this.source = source;
    }

    public int getCheckins() {
        return checkins;
    }

    public void setCheckins(int checkins) {
        this.checkins = checkins;
    }
}
