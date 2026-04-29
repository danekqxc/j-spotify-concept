package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);

        String query = getIntent().getStringExtra("SEARCH_QUERY");
        TextView artistNameView = findViewById(R.id.artist_name);
        ImageView artistPhotoView = findViewById(R.id.artist_photo);
        Button btnOpenBrowser = findViewById(R.id.btn_open_browser);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        if (query != null) {
            artistNameView.setText(query);
            Artist artist = MusicData.getArtistByName(query);
            if (artist != null && artist.getImageResId() != 0) {
                artistPhotoView.setImageResource(artist.getImageResId());
            } else {
                artistPhotoView.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            List<Album> artistAlbums = MusicData.getAlbumsByArtist(query);
            List<Track> artistTracks = MusicData.getTracksByArtist(query);

            RecyclerView recyclerAlbums = findViewById(R.id.recycler_artist_albums);
            ArtistAlbumAdapter albumAdapter = new ArtistAlbumAdapter(artistAlbums, album -> {
                Intent intent = new Intent(this, AlbumDetailsActivity.class);
                intent.putExtra("ALBUM", album);
                startActivity(intent);
            });
            recyclerAlbums.setLayoutManager(new LinearLayoutManager(this));
            recyclerAlbums.setAdapter(albumAdapter);
            recyclerAlbums.setNestedScrollingEnabled(false);

            RecyclerView recyclerTracks = findViewById(R.id.recycler_artist_tracks);
            TrackAdapter trackAdapter = new TrackAdapter(artistTracks, track -> {
                PlaybackManager.startTrack(this, track);
            });
            recyclerTracks.setLayoutManager(new LinearLayoutManager(this));
            recyclerTracks.setAdapter(trackAdapter);
            recyclerTracks.setNestedScrollingEnabled(false);
        }

        btnOpenBrowser.setOnClickListener(v -> {
            if (query != null && !query.isEmpty()) {
                openWebSearch(query);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }

    private void openWebSearch(String query) {
        String url = "https://www.google.com/search?q=" + Uri.encode(query);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Нет браузера", Toast.LENGTH_SHORT).show();
        }
    }
}
