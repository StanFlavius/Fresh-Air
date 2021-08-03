package com.example.freshair.Models.ModelsBreezoMeter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("datetime")
    @Expose
    private String datetime;

    @SerializedName("data_available")
    @Expose
    private String data_available;

    @SerializedName("indexes")
    @Expose
    private Indexes indexes;

    @SerializedName("pollutants")
    @Expose
    private Pollutants pollutants;

    @SerializedName("health_recommendations")
    @Expose
    private HealthRecommendations health_recommendations;

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

    public Indexes getIndexes() {
        return indexes;
    }

    public void setIndexes(Indexes indexes) {
        this.indexes = indexes;
    }

    public Pollutants getPollutants() {
        return pollutants;
    }

    public void setPollutants(Pollutants pollutants) {
        this.pollutants = pollutants;
    }

    public HealthRecommendations getHealth_recommendations() {
        return health_recommendations;
    }

    public void setHealth_recommendations(HealthRecommendations health_recommendations) {
        this.health_recommendations = health_recommendations;
    }
}
