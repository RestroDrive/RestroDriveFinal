package com.android.mno.restrodrive.restrodrive.Model;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;
import java.util.ArrayList;

public class Location implements Serializable {

    private String state;
    private String address3;
    private String crossStreets;
    private String address2;
    private String zipCode;
    private String city;
    private String country;
    private String address1;
    private ArrayList<String> displayAddress;

    @JsonGetter("state")
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }

    @JsonGetter("address3")
    public String getAddress3() {
        return this.address3;
    }
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    @JsonGetter("cross_streets")
    public String getCrossStreets() {
        return this.crossStreets;
    }
    public void setCrossStreets(String crossStreets) {
        this.crossStreets = crossStreets;
    }


    @JsonGetter("address2")
    public String getAddress2() {
        return this.address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @JsonGetter("zip_code")
    public String getZipCode() {
        return this.zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @JsonGetter("city")
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @JsonGetter("country")
    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonGetter("address1")
    public String getAddress1() {
        return this.address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @JsonGetter("display_address")
    public ArrayList<String> getDisplayAddress() {
        return this.displayAddress;
    }
    public void setDisplayAddress(ArrayList<String> displayAddress) {
        this.displayAddress = displayAddress;
    }

}
