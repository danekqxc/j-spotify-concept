package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class HomeCustomizationManager {
    private static final String PREFS_NAME = "home_prefs";
    private static final String KEY_FAVORITE_ARTISTS = "fav_artists";
    private static final String KEY_FAVORITE_ALBUMS = "fav_albums";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_AVATAR_URI = "avatar_uri";

    public static void addFavoriteArtist(Context context, String artistName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> artists = new HashSet<>(prefs.getStringSet(KEY_FAVORITE_ARTISTS, new HashSet<>()));
        artists.add(artistName);
        prefs.edit().putStringSet(KEY_FAVORITE_ARTISTS, artists).apply();
    }

    public static void removeFavoriteArtist(Context context, String artistName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> artists = new HashSet<>(prefs.getStringSet(KEY_FAVORITE_ARTISTS, new HashSet<>()));
        artists.remove(artistName);
        prefs.edit().putStringSet(KEY_FAVORITE_ARTISTS, artists).apply();
    }

    public static Set<String> getFavoriteArtists(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_FAVORITE_ARTISTS, new HashSet<>());
    }

    public static void addFavoriteAlbum(Context context, String albumTitle) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> albums = new HashSet<>(prefs.getStringSet(KEY_FAVORITE_ALBUMS, new HashSet<>()));
        albums.add(albumTitle);
        prefs.edit().putStringSet(KEY_FAVORITE_ALBUMS, albums).apply();
    }

    public static void removeFavoriteAlbum(Context context, String albumTitle) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> albums = new HashSet<>(prefs.getStringSet(KEY_FAVORITE_ALBUMS, new HashSet<>()));
        albums.remove(albumTitle);
        prefs.edit().putStringSet(KEY_FAVORITE_ALBUMS, albums).apply();
    }

    public static Set<String> getFavoriteAlbums(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_FAVORITE_ALBUMS, new HashSet<>());
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "test");
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public static String getAvatarUri(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_AVATAR_URI, null);
    }

    public static void setAvatarUri(Context context, String uri) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_AVATAR_URI, uri).apply();
    }
}