package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {
    public static final String CHANNEL_ID = "music_channel";
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREV = "action_prev";

    private MediaSessionCompat mediaSession;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        mediaSession = new MediaSessionCompat(this, "MusicService");
        mediaSession.setActive(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_PLAY:
                case ACTION_PAUSE:
                    PlaybackManager.togglePlayPause();
                    break;
                case ACTION_NEXT:
                    PlaybackManager.playNext(this);
                    break;
                case ACTION_PREV:
                    PlaybackManager.playPrev(this);
                    break;
            }
        }

        showNotification();
        return START_NOT_STICKY;
    }

    private void showNotification() {
        Track track = PlaybackManager.getCurrentTrack();
        if (track == null) return;

        updateMediaMetadata(track);
        updatePlaybackState();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        PendingIntent playPauseIntent = PendingIntent.getService(this, 1, 
                new Intent(this, MusicService.class).setAction(PlaybackManager.isPlaying() ? ACTION_PAUSE : ACTION_PLAY), 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        
        PendingIntent nextIntent = PendingIntent.getService(this, 2, 
                new Intent(this, MusicService.class).setAction(ACTION_NEXT), 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        PendingIntent prevIntent = PendingIntent.getService(this, 3, 
                new Intent(this, MusicService.class).setAction(ACTION_PREV), 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        int playPauseIcon = PlaybackManager.isPlaying() ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(), track.getImageResId() != 0 ? track.getImageResId() : R.drawable.ic_launcher_foreground);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(track.getTitle())
                .setContentText(track.getArtist())
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setLargeIcon(albumArt)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_media_previous, "Prev", prevIntent)
                .addAction(playPauseIcon, PlaybackManager.isPlaying() ? "Pause" : "Play", playPauseIntent)
                .addAction(android.R.drawable.ic_media_next, "Next", nextIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(PlaybackManager.isPlaying())
                .setSilent(true)
                .build();

        startForeground(1, notification);
    }

    private void updateMediaMetadata(Track track) {
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(), track.getImageResId() != 0 ? track.getImageResId() : R.drawable.ic_launcher_foreground);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, track.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, track.getArtist())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .build());
    }

    private void updatePlaybackState() {
        long position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | 
                            PlaybackStateCompat.ACTION_PAUSE | 
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT | 
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        
        int state = PlaybackManager.isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        stateBuilder.setState(state, position, 1.0f);
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Playback Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            serviceChannel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mediaSession != null) {
            mediaSession.release();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}