package com.example.myapplication;

public class Mood {
    private String title;
    private int color;
    private int iconRes;

    public Mood(String title, int color, int iconRes) {
        this.title = title;
        this.color = color;
        this.iconRes = iconRes;
    }

    public String getTitle() { return title; }
    public int getColor() { return color; }
    public int getIconRes() { return iconRes; }
}