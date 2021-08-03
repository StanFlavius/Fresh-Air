package com.example.freshair.Utils;

public class ItemNavBar {

    private String name;

    private Integer path;

    public ItemNavBar(String name, Integer path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPath() {
        return path;
    }

    public void setPath(Integer path) {
        this.path = path;
    }
}
