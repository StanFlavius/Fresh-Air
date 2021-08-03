package com.example.freshair.Models.ModelsBreezoMeter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pollutants {

    @SerializedName("co")
    @Expose
    private CO co;

    @SerializedName("no2")
    @Expose
    private NO2 no2;

    @SerializedName("o3")
    @Expose
    private O3 o3;

    @SerializedName("pm10")
    @Expose
    private PM10 pm10;

    @SerializedName("pm25")
    @Expose
    private PM25 pm25;

    @SerializedName("so2")
    @Expose
    private SO2 so2;

    public CO getCo() {
        return co;
    }

    public void setCo(CO co) {
        this.co = co;
    }

    public NO2 getNo2() {
        return no2;
    }

    public void setNo2(NO2 no2) {
        this.no2 = no2;
    }

    public O3 getO3() {
        return o3;
    }

    public void setO3(O3 o3) {
        this.o3 = o3;
    }

    public PM10 getPm10() {
        return pm10;
    }

    public void setPm10(PM10 pm10) {
        this.pm10 = pm10;
    }

    public PM25 getPm25() {
        return pm25;
    }

    public void setPm25(PM25 pm25) {
        this.pm25 = pm25;
    }

    public SO2 getSo2() {
        return so2;
    }

    public void setSo2(SO2 so2) {
        this.so2 = so2;
    }
}
