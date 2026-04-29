package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsListActivity extends AppCompatActivity {

    private static List<Playlist> playlistLibrary = new ArrayList<>();

    public static void addPlaylist(Playlist playlist) {
        playlistLibrary.add(playlist);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists_list);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycler_playlists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PlaylistAdapter adapter = new PlaylistAdapter(playlistLibrary, playlist -> {
            Toast.makeText(this, "Открываем: " + playlist.getName(), Toast.LENGTH_SHORT).show();
        });
        
        recyclerView.setAdapter(adapter);
    }
}