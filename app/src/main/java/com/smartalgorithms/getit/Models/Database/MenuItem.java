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
public class MenuItem {
    @Id
    private Long menuId;
    private Long placeId;
    private String title;
    private String description;
    private String imagePath;
    private String imageLink;
    private double price;
    private int currency;

    @Generated(hash = 1924699290)
    public MenuItem(Long menuId, Long placeId, String title, String description,
            String imagePath, String imageLink, double price, int currency) {
        this.menuId = menuId;
        this.placeId = placeId;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.imageLink = imageLink;
        this.price = price;
        this.currency = currency;
    }

    @Generated(hash = 1324140183)
    public MenuItem() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
