package com.example.freshair.Models.ModelsBreezoMeter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Indexes {

    @SerializedName("baqi")
    @Expose
    private Baqi baqi;

    @SerializedName("fra_atmo")
    @Expose
    private FraAtmo fra_atmo;

    public Baqi getBaqi() {
        return baqi;
    }

    public void setBaqi(Baqi baqi) {
        this.baqi = baqi;
    }

    public FraAtmo getFra_atmo() {
        return fra_atmo;
    }

    public void setFra_atmo(FraAtmo fra_atmo) {
        this.fra_atmo = fra_atmo;
    }
}
