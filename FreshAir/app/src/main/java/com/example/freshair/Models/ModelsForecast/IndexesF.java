package com.example.freshair.Models.ModelsForecast;

import com.example.freshair.Models.ModelsBreezoMeter.Baqi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndexesF {

    @SerializedName("baqi")
    @Expose
    private BaqiF baqiF;

    public BaqiF getBaqiF() {
        return baqiF;
    }

    public void setBaqiF(BaqiF baqiF) {
        this.baqiF = baqiF;
    }
}
