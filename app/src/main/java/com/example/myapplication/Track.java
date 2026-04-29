package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Track implements Serializable {
    private String title;
    private String artist;
    private boolean isLiked;
    private int audioResId;
    private int imageResId;
    private String youtubeVideoId;
    private List<Comment> comments = new ArrayList<>();

    public Track(String title, String artist) {
        this.title = title;
        this.artist = artist;
        this.isLiked = false;
    }

    public Track(String title, String artist, int audioResId, int imageResId) {
        this.title = title;
        this.artist = artist;
        this.audioResId = audioResId;
        this.imageResId = imageResId;
        this.isLiked = false;
    }

    public Track(String title, String artist, int audioResId, int imageResId, String youtubeVideoId) {
        this.title = title;
        this.artist = artist;
        this.audioResId = audioResId;
        this.imageResId = imageResId;
        this.youtubeVideoId = youtubeVideoId;
        this.isLiked = false;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { isLiked = liked; }
    public int getAudioResId() { return audioResId; }
    public int getImageResId() { return imageResId; }
    public String getYoutubeVideoId() { return youtubeVideoId; }
    public List<Comment> getComments() { return comments; }
    public void addComment(Comment comment) { comments.add(comment); }

    @Override
    public String toString() {
        return title + " — " + artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return Objects.equals(title, track.title) && Objects.equals(artist, track.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }
}