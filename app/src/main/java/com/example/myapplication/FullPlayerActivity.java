package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import java.util.List;

public class FullPlayerActivity extends AppCompatActivity {

    private TextView trackTitle, trackArtist, timeCurrent, timeTotal;
    private ImageView trackIcon, btnPlayPause, btnFavorite, btnPrev, btnNext, btnClose, btnComments;
    private SeekBar seekBar;
    private YouTubePlayerView youtubePlayerView;
    private YouTubePlayer currentYouTubePlayer;
    private String lastVideoId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_full);

        trackTitle = findViewById(R.id.track_title);
        trackArtist = findViewById(R.id.track_artist);
        timeCurrent = findViewById(R.id.time_current);
        timeTotal = findViewById(R.id.time_total);
        trackIcon = findViewById(R.id.full_track_icon);
        btnPlayPause = findViewById(R.id.btn_play_pause);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnClose = findViewById(R.id.btn_close);
        btnComments = findViewById(R.id.btn_comments);
        seekBar = findViewById(R.id.track_seek_bar);
        youtubePlayerView = findViewById(R.id.youtube_player_view);

        getLifecycle().addObserver(youtubePlayerView);

        btnClose.setOnClickListener(v -> finish());
        
        btnPrev.setOnClickListener(v -> {
            PlaybackManager.playPrev(this);
            updateUI();
        });

        btnNext.setOnClickListener(v -> {
            PlaybackManager.playNext(this);
            updateUI();
        });

        btnComments.setOnClickListener(v -> showCommentsDialog());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    PlaybackManager.seekTo(progress);
                    if (currentYouTubePlayer != null) {
                        currentYouTubePlayer.seekTo(progress / 1000f);
                    }
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        setupPlaybackManager();
        updateUI();
    }

    private void setupPlaybackManager() {
        PlaybackManager.setupPlayer(findViewById(android.R.id.content), v -> updateUI());
    }

    private void updateUI() {
        Track current = PlaybackManager.getCurrentTrack();
        if (current != null) {
            trackTitle.setText(current.getTitle());
            trackArtist.setText(current.getArtist());
            
            if (current.getYoutubeVideoId() != null) {
                trackIcon.setVisibility(View.GONE);
                youtubePlayerView.setVisibility(View.VISIBLE);
                
                if (!current.getYoutubeVideoId().equals(lastVideoId)) {
                    lastVideoId = current.getYoutubeVideoId();
                    loadYoutubeVideo(lastVideoId);
                } else if (currentYouTubePlayer != null) {
                    if (PlaybackManager.isPlaying()) {
                        currentYouTubePlayer.play();
                    } else {
                        currentYouTubePlayer.pause();
                    }
                }
            } else {
                trackIcon.setVisibility(View.VISIBLE);
                youtubePlayerView.setVisibility(View.GONE);
                trackIcon.setImageResource(current.getImageResId() != 0 ? current.getImageResId() : R.drawable.cow);
                if (currentYouTubePlayer != null) {
                    currentYouTubePlayer.pause();
                }
            }
        }
    }

    private void showCommentsDialog() {
        Track current = PlaybackManager.getCurrentTrack();
        if (current == null) return;

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_chat_sheet, null);
        bottomSheetDialog.setContentView(view);

        RecyclerView recyclerView = view.findViewById(R.id.chat_recycler_view);
        EditText input = view.findViewById(R.id.chat_input);
        ImageView btnSend = view.findViewById(R.id.btn_send_chat);

        List<Comment> comments = current.getComments();
        CommentAdapter adapter = new CommentAdapter(comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (!text.isEmpty()) {

                Comment newComment = new Comment("test", text, System.currentTimeMillis());
                current.addComment(newComment);
                adapter.notifyItemInserted(comments.size() - 1);
                recyclerView.scrollToPosition(comments.size() - 1);
                input.setText("");
            }
        });

        bottomSheetDialog.show();
    }

    private void loadYoutubeVideo(String videoId) {
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                currentYouTubePlayer = youTubePlayer;
                youTubePlayer.mute(); 
                youTubePlayer.loadVideo(videoId, 0);
                
                if (!PlaybackManager.isPlaying()) {
                    youTubePlayer.pause();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youtubePlayerView.release();
    }
}