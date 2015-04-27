package com.bxs.bnbtest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bhupinder on 16/4/15.
 */
public class BookModel implements Serializable {

    private String propertyId;
    private ArrayList<RoomModel> roomModelArrayList;

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public ArrayList<RoomModel> getRoomModelArrayList() {
        return roomModelArrayList;
    }

    public void setRoomModelArrayList(ArrayList<RoomModel> roomModelArrayList) {
        this.roomModelArrayList = roomModelArrayList;
    }
}
