package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Set;

public class CustomizationActivity extends AppCompatActivity {

    private LinearLayout containerArtists, containerAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        containerArtists = findViewById(R.id.container_artists);
        containerAlbums = findViewById(R.id.container_albums);
        AutoCompleteTextView searchBar = findViewById(R.id.search_customization);

        ArrayAdapter<Object> searchAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, MusicData.getAllForSearch());
        searchBar.setAdapter(searchAdapter);

        searchBar.setOnItemClickListener((parent, view, position, id) -> {
            Object selection = parent.getItemAtPosition(position);
            if (selection instanceof Artist) {
                String name = ((Artist) selection).getName();
                HomeCustomizationManager.addFavoriteArtist(this, name);
                Toast.makeText(this, "Артист добавлен на главный экран", Toast.LENGTH_SHORT).show();
            } else if (selection instanceof Album) {
                String title = ((Album) selection).getTitle();
                HomeCustomizationManager.addFavoriteAlbum(this, title);
                Toast.makeText(this, "Альбом добавлен на главный экран", Toast.LENGTH_SHORT).show();
            }
            searchBar.setText("");
            refreshLists();
        });
        refreshLists();
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }

    private void refreshLists() {
        containerArtists.removeAllViews();
        containerAlbums.removeAllViews();

        Set<String> selectedArtists = HomeCustomizationManager.getFavoriteArtists(this);
        Set<String> selectedAlbums = HomeCustomizationManager.getFavoriteAlbums(this);

        for (String artist : selectedArtists) {
            addCheckbox(containerArtists, artist, true, isChecked -> {
                if (!isChecked) HomeCustomizationManager.removeFavoriteArtist(this, artist);
            });
        }

        for (String album : selectedAlbums) {
            addCheckbox(containerAlbums, album, true, isChecked -> {
                if (!isChecked) HomeCustomizationManager.removeFavoriteAlbum(this, album);
            });
        }
    }

    private void addCheckbox(LinearLayout container, String text, boolean checked, OnCheckChanged listener) {
        CheckBox cb = new CheckBox(this);
        cb.setText(text);
        cb.setTextColor(getResources().getColor(android.R.color.white));
        cb.setChecked(checked);
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onChanged(isChecked);
            if (!isChecked) refreshLists();
        });
        container.addView(cb);
    }

    interface OnCheckChanged {
        void onChanged(boolean isChecked);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }
}