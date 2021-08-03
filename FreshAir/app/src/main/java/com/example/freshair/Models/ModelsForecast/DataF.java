package com.example.freshair.Models.ModelsForecast;

import com.example.freshair.Models.ModelsBreezoMeter.Indexes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataF {
    @SerializedName("datetime")
    @Expose
    private String datetime;

    @SerializedName("data_available")
    @Expose
    private String data_available;

    @SerializedName("indexes")
    @Expose
    private IndexesF indexesF;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getData_available() {
        return data_available;
    }

    public void setData_available(String data_available) {
        this.data_available = data_available;
    }

    public IndexesF getIndexesF() {
        return indexesF;
    }

    public void setIndexesF(IndexesF indexesF) {
        this.indexesF = indexesF;
    }
}
