package com.smartalgorithms.getit.Models.Database;


import com.smartalgorithms.getit.Models.Local.PhotoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
 */
//@Entity
public class PlaceInfo {
    //@Id
    private Long placeId;
    private String title;
    private String description = "";
    private String about = "";
    private String phone = "";
    private String link = "";
    private String icon = "";
    private List<PhotoData> imageLinks = new ArrayList<>();
    private List<String> imagePaths = new ArrayList<>();
    private int checkins = 0;
    private double latitude = 0;
    private double longitude = 0;
    private String address;
    private int uploaded;
    private int source;

    public PlaceInfo(Long placeId, String title, String description, String about, String phone, String link, String icon, List<PhotoData> imageLinks, List<String> imagePaths, int checkins, double latitude, double longitude, String address, int uploaded, int source) {
        this.placeId = placeId;
        this.title = title;
        this.description = description;
        this.about = about;
        this.phone = phone;
        this.link = link;
        this.icon = icon;
        this.imageLinks = imageLinks;
        this.imagePaths = imagePaths;
        this.checkins = checkins;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.uploaded = uploaded;
        this.source = source;
    }

    public PlaceInfo() {
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<PhotoData> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<PhotoData> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public int getCheckins() {
        return checkins;
    }

    public void setCheckins(int checkins) {
        this.checkins = checkins;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
