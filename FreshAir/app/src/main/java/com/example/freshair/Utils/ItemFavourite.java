package com.example.freshair.Utils;

public class ItemFavourite {

    private String address;

    private Integer value;

    private String description;

    private Double latitude;

    private Double longitude;

    public ItemFavourite(String address, Integer value, String description, Double latitude, Double longitude) {
        this.address = address;
        this.value = value;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        address = address;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
