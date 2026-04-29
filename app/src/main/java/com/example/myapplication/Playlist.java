package com.example.myapplication;

import java.io.Serializable;
import java.util.List;

public class Playlist implements Serializable {
    private String name;
    private List<Track> tracks;
    private String imageUri; // Путь к обложке

    public Playlist(String name, List<Track> tracks, String imageUri) {
        this.name = name;
        this.tracks = tracks;
        this.imageUri = imageUri;
    }

    public String getName() { return name; }
    public List<Track> getTracks() { return tracks; }
    public String getImageUri() { return imageUri; }
}