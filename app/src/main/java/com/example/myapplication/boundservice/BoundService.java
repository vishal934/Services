package com.example.myapplication.boundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.constants.Constants;

public class BoundService extends Service {
    public static final String CHANNEL_ID = "exampleServiceChannel";
    public Binder mBinder = new MyBindService();
    private static final String TAG = "BoundedService";
    public static final String MusicService = "MusicService";
    public MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate:");
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion: ");
                Intent intent = new Intent(MusicService);
                intent.putExtra(BoundMovieACtivity.MusicKey, "done");

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                stopSelf();
            }
        });

    }

    public class MyBindService extends Binder {

        public BoundService getService() {
            return BoundService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:" + intent.getAction());
        switch (intent.getAction()) {
            case Constants.Music_Start: {
                showNotification();
                break;
            }
            case Constants.Music_Play: {
                Log.d(TAG, "Music_Play: ");
                play();
                break;
            }
            case Constants.Music_Pause: {
                Log.d(TAG, "Music_Pause: ");
                pause();
                break;
            }
            case Constants.Music_Stop: {
                stopForeground(true);
                stopSelf();
            }
            default: {
            }
        }

        return START_NOT_STICKY;

    }

    private void showNotification() {
        Log.d(TAG, "Music_Pause: ");
        Intent pIntent = new Intent(this, BoundService.class);
        pIntent.setAction(Constants.Music_Play);
        PendingIntent playIntent = PendingIntent.getService(this, 100, pIntent, 0);


        Intent psIntent = new Intent(this, BoundService.class);
        psIntent.setAction(Constants.Music_Pause);
        PendingIntent pauseIntent = PendingIntent.getService(this, 100, psIntent, 0);

        Intent sIntent = new Intent(this, BoundService.class);
        sIntent.setAction(Constants.Music_Stop);
        PendingIntent stopIntent = PendingIntent.getService(this, 100, sIntent, 0);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("userInput")
                .setSmallIcon(R.mipmap.ic_launcher_round)

                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", playIntent))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "Pause", pauseIntent))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "Stop", stopIntent))
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Example Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
        startForeground(123, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mBinder;

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind:");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy:");
        super.onDestroy();
        mediaPlayer.release();
    }

    public boolean isPlaying() {
        Log.d(TAG, "isPlaying:" + mediaPlayer.isPlaying());
        return mediaPlayer.isPlaying();
    }

    public void play() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

}