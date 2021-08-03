package com.example.freshair.Models.ModelsBreezoMeter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaqiPoll {

    @SerializedName("display_name")
    @Expose
    private String display_name;

    @SerializedName("aqi")
    @Expose
    private Integer aqi;

    @SerializedName("aqi_display")
    @Expose
    private String aqi_display;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("category")
    @Expose
    private String category;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Integer getAqi() {
        return aqi;
    }

    public void setAqi(Integer aqi) {
        this.aqi = aqi;
    }

    public String getAqi_display() {
        return aqi_display;
    }

    public void setAqi_display(String aqi_display) {
        this.aqi_display = aqi_display;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
