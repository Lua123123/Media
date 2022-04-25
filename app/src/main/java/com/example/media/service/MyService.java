package com.example.media.service;

import static com.example.media.service.MyApplycation.CHANNEL_ID;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.media.R;
import com.example.media.fragment.Fragment1;


public class MyService extends Service {

    private MediaPlayer player;
    private MyBinder myBinder = new MyBinder();
    private boolean isPlaying;

    public class MyBinder extends Binder {
        public MyService getMyService() {
            return MyService.this;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMusic();

//        Intent intentMyService = new Intent(MyService.this, Fragment1.class);
//        intentMyService.putExtra("dataMyService", String.valueOf(player));
//        startActivity(intentMyService);

        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void startMusic() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.bathuongcon);
//            Settings.System.DEFAULT_RINGTONE_URI
        }
        player.start();
        sendNotification();
        isPlaying = true;
    }

    public void pauseMusic() {
        if (player != null && isPlaying) {
            player.pause();
            isPlaying = false;
        }
    }

    public void resumeMusic() {
        if (player != null && !isPlaying) {
            player.start();
            isPlaying = true;
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Media Player");
//        builder.setContentText(song.getName());
        builder.setContentText("Media Player Running");
        builder.setSmallIcon(R.drawable.ic_baseline_album_24);

        Notification notification = builder.build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            player.stop();
            player = null;
        }
        super.onDestroy();
    }
}
