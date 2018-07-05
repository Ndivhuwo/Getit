package com.smartalgorithms.getit.Models.Database;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
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
