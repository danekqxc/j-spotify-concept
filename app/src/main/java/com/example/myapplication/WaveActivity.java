package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WaveActivity extends AppCompatActivity {

    private RecyclerView recyclerMoods;
    private RecyclerView recyclerTracks;
    private TrackAdapter trackAdapter;
    private List<Track> waveTracks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        recyclerMoods = findViewById(R.id.recycler_moods);
        recyclerTracks = findViewById(R.id.recycler_wave_tracks);

        setupMoods();
        setupTracks();
        
        updatePlayerUI();
    }

    private void setupMoods() {
        List<Mood> moods = new ArrayList<>();
        int moodColor = Color.parseColor("#1A1A1A");
        
        moods.add(new Mood("Для работы", moodColor, android.R.drawable.ic_lock_idle_alarm));
        moods.add(new Mood("Для учебы", moodColor, android.R.drawable.ic_menu_edit));
        moods.add(new Mood("Тренировка", moodColor, android.R.drawable.ic_media_play));
        moods.add(new Mood("Отдых", moodColor, android.R.drawable.ic_menu_view));
        moods.add(new Mood("Агрессивная", moodColor, android.R.drawable.ic_delete));
        moods.add(new Mood("Спокойная", moodColor, android.R.drawable.ic_menu_day));

        MoodAdapter moodAdapter = new MoodAdapter(moods, mood -> {
            updateWaveTracks(mood.getTitle());
        });

        recyclerMoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerMoods.setAdapter(moodAdapter);
    }

    private void setupTracks() {
        trackAdapter = new TrackAdapter(waveTracks, track -> {
            PlaybackManager.startTrack(this, track);
            updatePlayerUI();
        });
        recyclerTracks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTracks.setAdapter(trackAdapter);
        
        updateWaveTracks("Для работы");
    }

    private void updateWaveTracks(String moodTitle) {
        waveTracks.clear();
        waveTracks.addAll(MusicData.getTracksByMood(moodTitle));
        trackAdapter.notifyDataSetChanged();
    }

    private void updatePlayerUI() {
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePlayerUI();
    }
}