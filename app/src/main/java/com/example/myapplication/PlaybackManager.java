package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.widget.ImageViewCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class PlaybackManager {
    private static Track currentTrack;
    private static boolean isPlaying = false;
    private static List<Track> likedTracks = new ArrayList<>();
    private static List<Track> playHistory = new ArrayList<>();
    private static List<Track> currentQueue = new ArrayList<>();
    private static int currentIndex = -1;
    
    private static MediaPlayer mediaPlayer;
    private static final Handler uiHandler = new Handler(Looper.getMainLooper());
    private static Runnable uiUpdateRunnable;
    private static Runnable waveAnimationRunnable;

    private static TextView activeTitle, activeArtist, activeTime, activeTimeCurrent, activeTimeTotal;
    private static ImageView activePlayPause, activeFavorite, activeIcon, activeNext, activePrev;
    private static ProgressBar activeProgressHorizontal;
    private static List<View> waveBars = new ArrayList<>();
    private static List<View> fullWaveBars = new ArrayList<>();
    private static List<View> backgroundWaveBars = new ArrayList<>();
    private static final Random random = new Random();

    public static void startTrack(Context context, Track track) {
        if (track == null) return;
        currentTrack = track;
        
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (track.getAudioResId() != 0) {
            mediaPlayer = MediaPlayer.create(context, track.getAudioResId());
            if (mediaPlayer != null) {
                mediaPlayer.start();
                isPlaying = true;
                mediaPlayer.setOnCompletionListener(mp -> playNext(context));
            }
        } else {
            isPlaying = true;
        }

        playHistory.remove(track);
        playHistory.add(0, track);
        
        if (!currentQueue.contains(track)) {
            currentQueue.clear();
            currentQueue.add(track);
            currentIndex = 0;
        } else {
            currentIndex = currentQueue.indexOf(track);
        }

        if (currentIndex >= currentQueue.size() - 1) {
            generateWave();
        }
        
        updateActiveUI();
        startUIUpdates();
        startWaveAnimation();
        updateService(context);
    }

    public static void startWave(Context context) {
        currentQueue.clear();
        generateWave();
        if (!currentQueue.isEmpty()) {
            currentIndex = 0;
            startTrack(context, currentQueue.get(0));
        }
    }

    public static void generateWave() {
        List<Track> allTracks = MusicData.getAllTracks();
        if (allTracks == null || allTracks.isEmpty()) return;
        
        List<Track> wave;
        if (likedTracks.isEmpty()) {
            wave = new ArrayList<>(allTracks);
            Collections.shuffle(wave);
            wave = wave.subList(0, Math.min(10, wave.size()));
        } else {
            Set<String> favArtists = likedTracks.stream()
                    .map(Track::getArtist)
                    .collect(Collectors.toSet());

            wave = allTracks.stream()
                    .filter(t -> favArtists.contains(t.getArtist()))
                    .filter(t -> !currentQueue.contains(t))
                    .collect(Collectors.toList());

            Collections.shuffle(wave);
            wave = wave.subList(0, Math.min(10, wave.size()));
            
            if (wave.size() < 5) {
                List<Track> randoms = new ArrayList<>(allTracks);
                Collections.shuffle(randoms);
                for (Track r : randoms) {
                    if (!currentQueue.contains(r) && !wave.contains(r)) wave.add(r);
                    if (wave.size() >= 10) break;
                }
            }
        }
        currentQueue.addAll(wave);
    }

    public static void playNext(Context context) {
        if (currentQueue.isEmpty()) return;
        currentIndex++;
        if (currentIndex >= currentQueue.size()) {
            generateWave();
        }
        startTrack(context, currentQueue.get(currentIndex));
    }

    public static void playPrev(Context context) {
        if (currentQueue.isEmpty() || currentIndex <= 0) return;
        currentIndex--;
        startTrack(context, currentQueue.get(currentIndex));
    }

    public static void togglePlayPause() {
        if (currentTrack == null) return;
        
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPlaying = false;
            } else {
                mediaPlayer.start();
                isPlaying = true;
            }
        } else {
            isPlaying = !isPlaying;
        }
        updateActiveUI();
        if (isPlaying) startWaveAnimation();
        
        if (activeTitle != null) {
            updateService(activeTitle.getContext());
        }
    }

    public static void seekTo(int progress) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(progress);
        }
    }

    private static void updateService(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, MusicService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }

    public static void toggleFavorite(Track track) {
        if (track == null) return;
        if (isFavorite(track)) {
            likedTracks.removeIf(t -> t.getTitle().equals(track.getTitle()) && t.getArtist().equals(track.getArtist()));
        } else {
            likedTracks.add(track);
        }
    }

    public static boolean isFavorite(Track track) {
        if (track == null) return false;
        for (Track t : likedTracks) {
            if (t.getTitle().equals(track.getTitle()) && t.getArtist().equals(track.getArtist())) return true;
        }
        return false;
    }

    public static List<Track> getLikedTracks() { return new ArrayList<>(likedTracks); }
    public static List<Track> getPlayHistory() { return new ArrayList<>(playHistory); }

    public static void setupPlayer(View rootView, View.OnClickListener onFavoriteChanged) {
        activeTitle = rootView.findViewById(R.id.track_title);
        activeArtist = rootView.findViewById(R.id.track_artist);
        activeTime = rootView.findViewById(R.id.track_time);
        activeTimeCurrent = rootView.findViewById(R.id.time_current);
        activeTimeTotal = rootView.findViewById(R.id.time_total);
        activePlayPause = rootView.findViewById(R.id.btn_play_pause);
        activeNext = rootView.findViewById(R.id.btn_next);
        activePrev = rootView.findViewById(R.id.btn_prev);
        activeProgressHorizontal = rootView.findViewById(R.id.track_progress_horizontal);
        if (activeProgressHorizontal == null) {
            activeProgressHorizontal = rootView.findViewById(R.id.track_seek_bar);
        }
        activeFavorite = rootView.findViewById(R.id.btn_favorite);
        activeIcon = rootView.findViewById(R.id.full_track_icon);
        if (activeIcon == null) {
            activeIcon = rootView.findViewById(R.id.item_track_icon);
        }

        waveBars.clear();
        int[] miniIds = {R.id.wave_bar1, R.id.wave_bar2, R.id.wave_bar3, R.id.wave_bar4};
        for (int id : miniIds) {
            View bar = rootView.findViewById(id);
            if (bar != null) waveBars.add(bar);
        }

        fullWaveBars.clear();
        int[] fullIds = {R.id.wave_bar_f1, R.id.wave_bar_f2, R.id.wave_bar_f3, R.id.wave_bar_f4, R.id.wave_bar_f5,
                         R.id.wave_bar_f6, R.id.wave_bar_f7, R.id.wave_bar_f8, R.id.wave_bar_f9, R.id.wave_bar_f10};
        for (int id : fullIds) {
            View bar = rootView.findViewById(id);
            if (bar != null) fullWaveBars.add(bar);
        }

        backgroundWaveBars.clear();
        for (int i = 1; i <= 13; i++) {
            int id = rootView.getResources().getIdentifier("wave_bg_bar" + i, "id", rootView.getContext().getPackageName());
            View bar = rootView.findViewById(id);
            if (bar != null) backgroundWaveBars.add(bar);
        }

        View progressWrapper = rootView.findViewById(R.id.progress_wrapper);
        if (progressWrapper != null) {
            progressWrapper.setOnClickListener(v -> {
                Context context = v.getContext();
                context.startActivity(new Intent(context, FullPlayerActivity.class));
            });
        }
        
        View waveContainer = rootView.findViewById(R.id.wave_container);
        if (waveContainer != null) {
            waveContainer.setOnClickListener(v -> {
                Context context = v.getContext();
                context.startActivity(new Intent(context, FullPlayerActivity.class));
            });
        }

        if (activePlayPause != null) {
            activePlayPause.setOnClickListener(v -> togglePlayPause());
        }
        
        if (activeNext != null) {
            activeNext.setOnClickListener(v -> playNext(v.getContext()));
        }
        
        if (activePrev != null) {
            activePrev.setOnClickListener(v -> playPrev(v.getContext()));
        }

        if (activeFavorite != null) {
            activeFavorite.setOnClickListener(v -> {
                toggleFavorite(currentTrack);
                updateActiveUI();
                if (onFavoriteChanged != null) onFavoriteChanged.onClick(v);
            });
        }
        
        if (activeProgressHorizontal instanceof SeekBar) {
            ((SeekBar) activeProgressHorizontal).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mediaPlayer != null) {
                        mediaPlayer.seekTo(progress);
                    }
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        updateActiveUI();
        startUIUpdates();
        startWaveAnimation();
    }

    public static void updateActiveUI() {
        if (activeTitle == null) return;

        if (currentTrack != null) {
            activeTitle.setText(currentTrack.getTitle());
            if (activeArtist != null) activeArtist.setText(currentTrack.getArtist());
            if (activePlayPause != null) {
                activePlayPause.setImageResource(isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
            }
            if (activeIcon != null) {
                if (currentTrack.getImageResId() != 0) {
                    activeIcon.setImageResource(currentTrack.getImageResId());
                } else {
                    activeIcon.setImageDrawable(null);
                }
            }
            
            if (mediaPlayer != null) {
                int current = mediaPlayer.getCurrentPosition();
                int total = mediaPlayer.getDuration();
                if (activeProgressHorizontal != null) {
                    activeProgressHorizontal.setMax(total);
                    activeProgressHorizontal.setProgress(current);
                }
                if (activeTime != null) activeTime.setText(formatTime(current) + " / " + formatTime(total));
                if (activeTimeCurrent != null) activeTimeCurrent.setText(formatTime(current));
                if (activeTimeTotal != null) activeTimeTotal.setText(formatTime(total));
            } else {
                if (activeProgressHorizontal != null) activeProgressHorizontal.setProgress(0);
                if (activeTime != null) activeTime.setText("00:00 / 00:00");
                if (activeTimeCurrent != null) activeTimeCurrent.setText("00:00");
                if (activeTimeTotal != null) activeTimeTotal.setText("00:00");
            }
            
            if (activeFavorite != null) {
                boolean favorite = isFavorite(currentTrack);
                ImageViewCompat.setImageTintList(activeFavorite, ColorStateList.valueOf(favorite ? Color.RED : Color.parseColor("#B3B3B3")));
            }
        } else {
            activeTitle.setText("не воспроизводится");
            if (activeArtist != null) activeArtist.setText("неизвестен");
            if (activePlayPause != null) activePlayPause.setImageResource(android.R.drawable.ic_media_play);
            if (activeProgressHorizontal != null) activeProgressHorizontal.setProgress(0);
            if (activeTime != null) activeTime.setText("00:00 / 00:00");
            if (activeTimeCurrent != null) activeTimeCurrent.setText("00:00");
            if (activeTimeTotal != null) activeTimeTotal.setText("00:00");
            if (activeIcon != null) activeIcon.setImageDrawable(null);
            if (activeFavorite != null) {
                ImageViewCompat.setImageTintList(activeFavorite, ColorStateList.valueOf(Color.parseColor("#B3B3B3")));
            }
        }
    }

    private static String formatTime(int ms) {
        int minutes = (ms / 1000) / 60;
        int seconds = (ms / 1000) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private static void startUIUpdates() {
        if (uiUpdateRunnable != null) return;

        uiUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                if (isPlaying) {
                    updateActiveUI();
                }
                uiHandler.postDelayed(this, 1000);
            }
        };
        uiHandler.post(uiUpdateRunnable);
    }

    private static void startWaveAnimation() {
        if (waveAnimationRunnable != null) return;

        waveAnimationRunnable = new Runnable() {
            @Override
            public void run() {
                if (isPlaying) {
                    if (!waveBars.isEmpty()) {
                        Context context = waveBars.get(0).getContext();
                        float baseHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
                        float maxExtra = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, context.getResources().getDisplayMetrics());

                        for (View bar : waveBars) {
                            ViewGroup.LayoutParams params = bar.getLayoutParams();
                            params.height = (int) (baseHeight + random.nextFloat() * maxExtra);
                            bar.setLayoutParams(params);
                        }
                    }

                    if (!fullWaveBars.isEmpty()) {
                        Context context = fullWaveBars.get(0).getContext();
                        float baseHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
                        float maxExtra = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, context.getResources().getDisplayMetrics());

                        for (View bar : fullWaveBars) {
                            ViewGroup.LayoutParams params = bar.getLayoutParams();
                            params.height = (int) (baseHeight + random.nextFloat() * maxExtra);
                            bar.setLayoutParams(params);
                        }
                    }

                    if (!backgroundWaveBars.isEmpty()) {
                        Context context = backgroundWaveBars.get(0).getContext();
                        float baseHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
                        float maxExtra = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());

                        for (View bar : backgroundWaveBars) {
                            ViewGroup.LayoutParams params = bar.getLayoutParams();
                            params.height = (int) (baseHeight + random.nextFloat() * maxExtra);
                            bar.setLayoutParams(params);
                        }
                    }
                    
                    if (!waveBars.isEmpty() || !fullWaveBars.isEmpty() || !backgroundWaveBars.isEmpty()) {
                        uiHandler.postDelayed(this, 150);
                    } else {
                        waveAnimationRunnable = null;
                    }
                } else {
                    resetWaveBars();
                    waveAnimationRunnable = null;
                }
            }
        };
        uiHandler.post(waveAnimationRunnable);
    }

    private static void resetWaveBars() {
        if (!waveBars.isEmpty()) {
            Context context = waveBars.get(0).getContext();
            int defaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
            for (View bar : waveBars) {
                ViewGroup.LayoutParams params = bar.getLayoutParams();
                params.height = defaultHeight;
                bar.setLayoutParams(params);
            }
        }
        if (!fullWaveBars.isEmpty()) {
            Context context = fullWaveBars.get(0).getContext();
            int defaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
            for (View bar : fullWaveBars) {
                ViewGroup.LayoutParams params = bar.getLayoutParams();
                params.height = defaultHeight;
                bar.setLayoutParams(params);
            }
        }
        if (!backgroundWaveBars.isEmpty()) {
            Context context = backgroundWaveBars.get(0).getContext();
            int defaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
            for (View bar : backgroundWaveBars) {
                ViewGroup.LayoutParams params = bar.getLayoutParams();
                params.height = defaultHeight;
                bar.setLayoutParams(params);
            }
        }
    }

    public static Track getCurrentTrack() { return currentTrack; }
    public static boolean isPlaying() { return isPlaying; }
}