package com.example.freshair.Models.ModelsBreezoMeter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AqiInformation {

    @SerializedName("baqi")
    @Expose
    private BaqiPoll baqiPoll;

    public BaqiPoll getBaqiPoll() {
        return baqiPoll;
    }

    public void setBaqiPoll(BaqiPoll baqiPoll) {
        this.baqiPoll = baqiPoll;
    }
}
