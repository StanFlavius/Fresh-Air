package com.example.freshair.Models.ModelsForecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataBMF {
    @SerializedName("metadata")
    @Expose
    private String metadata;

    @SerializedName("data")
    @Expose
    private List<DataF> dataF;

    @SerializedName("error")
    @Expose
    private String Error;

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public List<DataF> getDataF() {
        return dataF;
    }

    public void setDataF(List<DataF> dataF) {
        this.dataF = dataF;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}
