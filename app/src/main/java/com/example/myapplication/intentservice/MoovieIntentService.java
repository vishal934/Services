package com.example.myapplication.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class MoovieIntentService extends IntentService {
    private static final String TAG = "MovieIntentService";
    public static final String IntentService = "IntentService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        Log.d(TAG, "onCreate: "+Thread.currentThread().getName());
    }

    public MoovieIntentService( ) {
        super("MovieIntentService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){

            String movieName = intent.getStringExtra(MovieActivity.KEY);
            downloadMovie(movieName);
          sendNotificationToUI(movieName);

        }


    }

    private void sendNotificationToUI(String movieName) {
        Intent intent = new Intent(IntentService);
        intent.putExtra(MovieActivity.KEY,movieName);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);


    }

    private void downloadMovie(String movieName) {
        Log.d(TAG,"Start Downloading:");
        try{
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"Completed Downloading:"+movieName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: "+Thread.currentThread().getName());
    }
}