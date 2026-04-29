package com.example.myapplication;

import java.io.Serializable;

public class Artist implements Serializable {
    private String name;
    private int imageResId;

    public Artist(String name) {
        this.name = name;
    }

    public Artist(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    @Override
    public String toString() {
        return name;
    }
}