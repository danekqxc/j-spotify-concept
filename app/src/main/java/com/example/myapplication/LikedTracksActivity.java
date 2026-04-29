package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LikedTracksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TrackAdapter adapter;
    private List<Track> allLikedTracks;
    private List<Track> filteredTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_tracks);

        setupPlayer();

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_liked_tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        updateList();

        EditText searchBar = findViewById(R.id.search_liked);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupPlayer() {
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), v -> {
            updateList();
        });
    }

    private void updateList() {
        allLikedTracks = PlaybackManager.getLikedTracks();
        filteredTracks = new ArrayList<>(allLikedTracks);
        adapter = new TrackAdapter(filteredTracks, track -> {
            PlaybackManager.startTrack(this, track);
        });
        recyclerView.setAdapter(adapter);
    }

    private void filter(String text) {
        filteredTracks.clear();
        for (Track track : allLikedTracks) {
            if (track.getTitle().toLowerCase().contains(text.toLowerCase()) || 
                track.getArtist().toLowerCase().contains(text.toLowerCase())) {
                filteredTracks.add(track);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupPlayer();
        updateList();
    }
}