package com.example.myapplication;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {
    private String title;
    private String artist;
    private List<Track> tracks;
    private int imageResId;

    public Album(String title, String artist, List<Track> tracks) {
        this.title = title;
        this.artist = artist;
        this.tracks = tracks;
    }

    public Album(String title, String artist, List<Track> tracks, int imageResId) {
        this.title = title;
        this.artist = artist;
        this.tracks = tracks;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public List<Track> getTracks() { return tracks; }
    public int getImageResId() { return imageResId; }

    @Override
    public String toString() {
        return "Альбом: " + title + " — " + artist;
    }
}