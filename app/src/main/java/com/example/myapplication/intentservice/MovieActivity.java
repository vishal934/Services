package com.example.myapplication.intentservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.R;

public class MovieActivity extends AppCompatActivity {
   public static final String KEY ="ServiceKey";

   BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
       @Override
       public void onReceive(Context context, Intent intent) {
     String name =intent.getStringExtra(KEY);
           Log.d("TAG","onReceive:"+name);
       }
   };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver,new IntentFilter(MoovieIntentService.IntentService));
    }

    public void startService(View view) {
        for(String name:MovieName.movieList)
        {
            Intent intent = new Intent(this,MoovieIntentService.class);
            intent.putExtra(MovieActivity.KEY,name);
            startService(intent);
        }
    }

    public void stopService(View view) {
        Intent intent = new Intent (this,MoovieIntentService.class);
        stopService(intent);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);


    }
}

class MovieName {
    public static final String[] movieList = {"AAA", "BBBB", "CCCC"};
}