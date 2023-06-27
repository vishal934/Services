package com.example.myapplication.foregroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "exampleServiceChannel";

    public ForegroundService() {
    }

    @Override
    public void onCreate() {
        Log.d("MTAG", "Oncreate ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        shownotification();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i <= 20) {
                    Log.d("MTAG", "run: " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("MTAG", "runCompleted: " + i);
                    i++;
                }
                stopForeground(true);
                stopSelf();
            }
        }).start();

        return START_STICKY;
    }

    private void shownotification() {
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId");
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Title")
//                .setContentText("This is Notification");
//        Notification notification = builder.build();
//        Log.d("MTAG", "showNotification: "+notification);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("userinput")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .build();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,"Example Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
        startForeground(123, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("MTAG", "Destroy: ");
        super.onDestroy();

    }
}