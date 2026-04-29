package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        Album album = (Album) getIntent().getSerializableExtra("ALBUM");
        if (album != null) {
            ((TextView) findViewById(R.id.album_title_large)).setText(album.getTitle());
            ((TextView) findViewById(R.id.album_artist_large)).setText(album.getArtist());
            
            ImageView albumCover = findViewById(R.id.album_cover_large);
            if (album.getImageResId() != 0) {
                albumCover.setImageResource(album.getImageResId());
            }

            RecyclerView recyclerView = findViewById(R.id.recycler_album_tracks);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            
            TrackAdapter adapter = new TrackAdapter(album.getTracks(), track -> {
                PlaybackManager.startTrack(this, track);
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }
}
