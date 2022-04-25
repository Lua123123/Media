package com.example.media.service;

import static com.example.media.service.MyApplycation.CHANNEL_ID;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.media.R;

import java.io.IOException;

public class MyServiceUrl extends Service {

    private MediaPlayer playerFromUrl;
    private boolean isPlaying;
    private MyBinderFromUrl myBinderFromUrl = new MyBinderFromUrl();

    //    public static final String URL_FILE = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
    public class MyBinderFromUrl extends Binder {
        public MyServiceUrl getMyServiceFromUrl() {
            return MyServiceUrl.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent2) {
        return myBinderFromUrl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String url = (String) bundle.get("Data");
            startMusicFromUrl(url);
        }
        return START_NOT_STICKY;
    }

    private void startMusicFromUrl(String url) {
        playerFromUrl = new MediaPlayer();
        playerFromUrl.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            playerFromUrl.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerFromUrl.prepareAsync();
        playerFromUrl.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        playerFromUrl.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });
        sendNotification();
        isPlaying = true;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Media Player");
//        builder.setContentText(song.getName());
        builder.setContentText("Media Player From Url Running");
        builder.setSmallIcon(R.drawable.ic_baseline_album_24);

        Notification notification = builder.build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        if (playerFromUrl != null) {
            playerFromUrl.release();
            playerFromUrl = null;
        }
        super.onDestroy();
    }
}
