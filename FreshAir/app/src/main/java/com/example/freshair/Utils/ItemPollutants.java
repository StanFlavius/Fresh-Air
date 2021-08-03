package com.example.freshair.Utils;

public class ItemPollutants {

    private String name;

    private Double value;

    private String unitM;

    public ItemPollutants(String name, Double value, String unitM) {
        this.name = name;
        this.value = value;
        this.unitM = unitM;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnitM() {
        return unitM;
    }

    public void setUnitM(String unitM) {
        this.unitM = unitM;
    }
}
