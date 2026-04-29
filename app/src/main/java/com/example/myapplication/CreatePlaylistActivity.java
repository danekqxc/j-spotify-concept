package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CreatePlaylistActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private ImageView playlistCover;
    private Uri imageUri;
    
    private List<Track> playlistTracks = new ArrayList<>();
    private TrackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        playlistCover = findViewById(R.id.playlist_cover);
        Button btnUpload = findViewById(R.id.btn_upload_cover);
        Button btnCreate = findViewById(R.id.btn_create_final);
        EditText editName = findViewById(R.id.edit_playlist_name);
        AutoCompleteTextView searchTracksBar = findViewById(R.id.search_tracks_bar);
        RecyclerView recyclerView = findViewById(R.id.recycler_tracks);

        adapter = new TrackAdapter(playlistTracks, track -> {
            PlaybackManager.startTrack(this, track);
            PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ArrayAdapter<Object> searchAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, MusicData.getAllForSearch());
        searchTracksBar.setAdapter(searchAdapter);

        searchTracksBar.setOnItemClickListener((parent, view, position, id) -> {
            Object selection = parent.getItemAtPosition(position);
            if (selection instanceof Track) {
                Track track = (Track) selection;
                if (!playlistTracks.contains(track)) {
                    playlistTracks.add(track);
                    adapter.notifyDataSetChanged();
                    searchTracksBar.setText("");
                    Toast.makeText(this, "Добавлено: " + track.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Уже в списке", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Выберите трек, а не альбом", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpload.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        });

        btnCreate.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show();
            } else if (playlistTracks.isEmpty()) {
                Toast.makeText(this, "Добавьте треки", Toast.LENGTH_SHORT).show();
            } else {
                Playlist newPlaylist = new Playlist(name, new ArrayList<>(playlistTracks), 
                        imageUri != null ? imageUri.toString() : null);
                PlaylistsListActivity.addPlaylist(newPlaylist);
                finish();
            }
        });

        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            imageUri = data.getData();
            playlistCover.setImageURI(imageUri);
            playlistCover.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }
}
