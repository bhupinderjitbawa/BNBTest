package com.bxs.bnbtest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bhupinder on 5/4/15.
 */
public class PropertyModel implements Serializable {

    private String id;
    private String name;
    private String phone;
    private String picture;
    private String address;
    private String description;
    private boolean cvvRequired;
    private String  taxRate;
    private ArrayList<RoomModel> roomModelArrayList;


    public boolean isCvvRequired() {
        return cvvRequired;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public void setCvvRequired(boolean cvvRequired) {
        this.cvvRequired = cvvRequired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<RoomModel> getRoomModelArrayList() {
        return roomModelArrayList;
    }

    public void setRoomModelArrayList(ArrayList<RoomModel> roomModelArrayList) {
        this.roomModelArrayList = roomModelArrayList;
    }
}
