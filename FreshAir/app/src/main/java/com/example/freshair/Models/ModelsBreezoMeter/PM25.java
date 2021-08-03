package com.example.freshair.Models.ModelsBreezoMeter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PM25 {

    @SerializedName("display_name")
    @Expose
    private String display_name;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("aqi_information")
    @Expose
    private AqiInformation aqi_inforamtion;

    @SerializedName("concentration")
    @Expose
    private Concentration concentration;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public AqiInformation getAqi_inforamtion() {
        return aqi_inforamtion;
    }

    public void setAqi_inforamtion(AqiInformation aqi_inforamtion) {
        this.aqi_inforamtion = aqi_inforamtion;
    }

    public Concentration getConcentration() {
        return concentration;
    }

    public void setConcentration(Concentration concentration) {
        this.concentration = concentration;
    }
}
