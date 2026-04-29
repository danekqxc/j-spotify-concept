package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView searchBar;
    private RecyclerView recyclerArtists, recyclerAlbums, recyclerRecent;
    private TextView labelArtists, labelAlbums, greetingTextView;
    private ImageView profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        greetingTextView = findViewById(R.id.main_greeting);
        profileIcon = findViewById(R.id.profile_icon);

        // Навигация
        profileIcon.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        findViewById(R.id.btn_playlists_list).setOnClickListener(v -> startActivity(new Intent(this, PlaylistsListActivity.class)));
        findViewById(R.id.btn_add_playlist).setOnClickListener(v -> startActivity(new Intent(this, CreatePlaylistActivity.class)));
        findViewById(R.id.btn_liked_tracks).setOnClickListener(v -> startActivity(new Intent(this, LikedTracksActivity.class)));
        
        // Кнопка "Моя волна"
        findViewById(R.id.btn_start_wave).setOnClickListener(v -> startActivity(new Intent(this, WaveActivity.class)));

        // Поиск
        searchBar = findViewById(R.id.search_bar);
        ArrayAdapter<Object> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, MusicData.getAllForSearch());
        searchBar.setAdapter(adapter);

        searchBar.setOnItemClickListener((parent, view, position, id) -> {
            Object selection = parent.getItemAtPosition(position);
            if (selection instanceof Artist) {
                openArtistDetails(((Artist) selection).getName());
            } else if (selection instanceof Album) {
                Intent intent = new Intent(this, AlbumDetailsActivity.class);
                intent.putExtra("ALBUM", (Album) selection);
                startActivity(intent);
            } else if (selection instanceof Track) {
                PlaybackManager.startTrack(this, (Track) selection);
                updatePlayerUI();
                loadRecentTracks();
            }
            searchBar.setText("");
        });

        recyclerArtists = findViewById(R.id.recycler_home_artists);
        recyclerAlbums = findViewById(R.id.recycler_home_albums);
        recyclerRecent = findViewById(R.id.recycler_recent);
        labelArtists = findViewById(R.id.label_my_artists);
        labelAlbums = findViewById(R.id.label_my_albums);

        loadCustomContent();
        loadRecentTracks();
        updatePlayerUI();
        updateGreeting();
        updateProfileIcon();
    }

    private void updateGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour >= 6 && hour < 11) greeting = "Доброе утро";
        else if (hour >= 11 && hour < 16) greeting = "Добрый день";
        else if (hour >= 16 && hour < 21) greeting = "Добрый вечер";
        else greeting = "Доброй ночи";
        
        greetingTextView.setText(greeting + ", " + HomeCustomizationManager.getUsername(this));
    }

    private void updateProfileIcon() {
        String avatarUriStr = HomeCustomizationManager.getAvatarUri(this);
        if (avatarUriStr != null) {
            profileIcon.setImageURI(Uri.parse(avatarUriStr));
            profileIcon.setPadding(0, 0, 0, 0);
        }
    }

    private void loadCustomContent() {
        recyclerArtists.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerAlbums.setLayoutManager(new GridLayoutManager(this, 2));

        Set<String> favArtistNames = HomeCustomizationManager.getFavoriteArtists(this);
        List<Object> displayArtists = new ArrayList<>();
        for (String name : favArtistNames) {
            Artist artist = MusicData.getArtistByName(name);
            if (artist != null) displayArtists.add(artist);
        }

        if (!displayArtists.isEmpty()) {
            labelArtists.setVisibility(View.VISIBLE);
            recyclerArtists.setVisibility(View.VISIBLE);
            WideAdapter adapter = new WideAdapter(displayArtists, item -> {
                if (item instanceof Artist) openArtistDetails(((Artist) item).getName());
            });
            recyclerArtists.setAdapter(adapter);
        } else {
            labelArtists.setVisibility(View.GONE);
            recyclerArtists.setVisibility(View.GONE);
        }

        Set<String> favAlbumTitles = HomeCustomizationManager.getFavoriteAlbums(this);
        List<Object> displayAlbums = new ArrayList<>();
        for (String title : favAlbumTitles) {
            Album album = MusicData.getAlbumByTitle(title);
            if (album != null) displayAlbums.add(album);
        }

        if (!displayAlbums.isEmpty()) {
            labelAlbums.setVisibility(View.VISIBLE);
            recyclerAlbums.setVisibility(View.VISIBLE);
            WideAdapter adapter = new WideAdapter(displayAlbums, item -> {
                if (item instanceof Album) {
                    Intent intent = new Intent(this, AlbumDetailsActivity.class);
                    intent.putExtra("ALBUM", (Album) item);
                    startActivity(intent);
                }
            });
            recyclerAlbums.setAdapter(adapter);
        } else {
            labelAlbums.setVisibility(View.GONE);
            recyclerAlbums.setVisibility(View.GONE);
        }
    }

    private void loadRecentTracks() {
        List<Track> history = PlaybackManager.getPlayHistory();
        List<Track> displayRecent;

        if (history.isEmpty()) {
            displayRecent = MusicData.getAllTracks();
            Collections.shuffle(displayRecent);
            if (displayRecent.size() > 8) displayRecent = displayRecent.subList(0, 8);
        } else {
            displayRecent = new ArrayList<>(history);
        }

        RecentTracksAdapter recentAdapter = new RecentTracksAdapter(displayRecent, track -> {
            PlaybackManager.startTrack(this, track);
            updatePlayerUI();
            loadRecentTracks();
        });
        recyclerRecent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerRecent.setAdapter(recentAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchBar != null) searchBar.setText("");
        loadCustomContent();
        loadRecentTracks();
        updatePlayerUI();
        updateGreeting();
        updateProfileIcon();
    }

    private void updatePlayerUI() {
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }

    private void openArtistDetails(String query) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("SEARCH_QUERY", query);
        startActivity(intent);
    }
}