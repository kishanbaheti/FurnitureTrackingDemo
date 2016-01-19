package com.furnituretrackingdemo.model;

import java.io.Serializable;

public class FeedItem implements Serializable {

    private int id;
    private String name;
    private String description;
    private String img_path;
    private String location;
    private int cost;

    public int getId() {
        return id;
    }

    public void setId(int val) {
        this.id = val;
    }

    public String getName() {
        return name;
    }

    public void setName(String val) {
        this.name = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String val) {
        this.description = val;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String val) {
        this.img_path = val;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String val) {
        this.location = val;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int val) {
        this.cost = val;
    }
}