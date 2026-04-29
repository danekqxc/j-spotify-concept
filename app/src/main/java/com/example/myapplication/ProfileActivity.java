package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_AVATAR = 200;
    private ImageView profileImageLarge;
    private TextView usernameView;
    private Uri pendingAvatarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageLarge = findViewById(R.id.profile_image_large);
        usernameView = findViewById(R.id.username);

        // Загрузка текущих данных
        usernameView.setText(HomeCustomizationManager.getUsername(this));
        String avatarUriStr = HomeCustomizationManager.getAvatarUri(this);
        if (avatarUriStr != null) {
            profileImageLarge.setImageURI(Uri.parse(avatarUriStr));
            profileImageLarge.setPadding(0, 0, 0, 0);
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_settings).setOnClickListener(v -> {
            startActivity(new Intent(this, CustomizationActivity.class));
        });

        usernameView.setOnClickListener(v -> showEditProfileDialog());
        setupTopArtists();
        updateHistory();
        
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }

    private void setupTopArtists() {
        RecyclerView recyclerArtists = findViewById(R.id.recycler_top_artists);
        List<Album> mockArtists = new ArrayList<>();
        mockArtists.addAll(MusicData.getAlbumsByArtist("FRIENDLY THUG"));
        mockArtists.addAll(MusicData.getAlbumsByArtist("ALBLAK"));
        
        ArtistAlbumAdapter artistAdapter = new ArtistAlbumAdapter(mockArtists, album -> {
            Intent intent = new Intent(this, AlbumDetailsActivity.class);
            intent.putExtra("ALBUM", album);
            startActivity(intent);
        });
        recyclerArtists.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerArtists.setAdapter(artistAdapter);
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        EditText editUsername = dialogView.findViewById(R.id.edit_username_dialog);
        ImageView editAvatar = dialogView.findViewById(R.id.edit_avatar_preview);
        Button btnSelectImage = dialogView.findViewById(R.id.btn_select_avatar);
        Button btnSave = dialogView.findViewById(R.id.btn_save_profile);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel_profile);

        editUsername.setText(HomeCustomizationManager.getUsername(this));
        String currentUriStr = HomeCustomizationManager.getAvatarUri(this);
        if (currentUriStr != null) {
            editAvatar.setImageURI(Uri.parse(currentUriStr));
        }
        pendingAvatarUri = currentUriStr != null ? Uri.parse(currentUriStr) : null;

        AlertDialog dialog = builder.create();

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_AVATAR);
        });

        btnSave.setOnClickListener(v -> {
            String newName = editUsername.getText().toString().trim();
            if (!newName.isEmpty()) {
                HomeCustomizationManager.setUsername(this, newName);
                usernameView.setText(newName);
                if (pendingAvatarUri != null) {
                    HomeCustomizationManager.setAvatarUri(this, pendingAvatarUri.toString());
                    profileImageLarge.setImageURI(pendingAvatarUri);
                    profileImageLarge.setPadding(0, 0, 0, 0);
                }
                dialog.dismiss();
                Toast.makeText(this, "Профиль обновлен", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_AVATAR && data != null) {
            pendingAvatarUri = data.getData();
            getContentResolver().takePersistableUriPermission(pendingAvatarUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Toast.makeText(this, "Фото выбрано", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHistory() {
        RecyclerView recyclerHistory = findViewById(R.id.recycler_history);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        List<Track> history = PlaybackManager.getPlayHistory();
        TrackAdapter historyAdapter = new TrackAdapter(history, track -> {
            PlaybackManager.startTrack(this, track);
            PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
        });
        recyclerHistory.setAdapter(historyAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHistory();
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), null);
    }
}