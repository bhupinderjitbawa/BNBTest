package com.bxs.bnbtest;

import java.io.Serializable;

/**
 * Created by bhupinder on 12/4/15.
 */
public class GeoModel implements Serializable {

    private String id;
    private String name;
    private String state;
    private String country;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
