package com.example.freshair.Models.ModelsBreezoMeter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthRecommendations {

    @SerializedName("general_population")
    @Expose
    private String general_population;

    @SerializedName("elderly")
    @Expose
    private String elderly;

    @SerializedName("lung_diseases")
    @Expose
    private String lung_diseases;

    @SerializedName("heart_diseases")
    @Expose
    private String heart_diseases;

    @SerializedName("active")
    @Expose
    private String active;

    @SerializedName("pregnant_woman")
    @Expose
    private String pregnant_woman;

    @SerializedName("children")
    @Expose
    private String children;

    public String getGeneral_population() {
        return general_population;
    }

    public void setGeneral_population(String general_population) {
        this.general_population = general_population;
    }

    public String getElderly() {
        return elderly;
    }

    public void setElderly(String elderly) {
        this.elderly = elderly;
    }

    public String getLung_diseases() {
        return lung_diseases;
    }

    public void setLung_diseases(String lung_diseases) {
        this.lung_diseases = lung_diseases;
    }

    public String getHeart_diseases() {
        return heart_diseases;
    }

    public void setHeart_diseases(String heart_diseases) {
        this.heart_diseases = heart_diseases;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPregnant_woman() {
        return pregnant_woman;
    }

    public void setPregnant_woman(String pregnant_woman) {
        this.pregnant_woman = pregnant_woman;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }
}
