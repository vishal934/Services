package com.example.myapplication.boundservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.foregroundservice.ForegroundService;

public class BoundMovieACtivity extends AppCompatActivity {
    private static final String TAG = "BoundedService";
    public static final String MusicKey = "musicKey";
    public Button btnSong;
    public BoundService mBoundeService;
    public boolean mBound = false;
    TextView textView;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            BoundService.MyBindService myBindService =(BoundService.MyBindService) service;
             mBoundeService =myBindService.getService();
             mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_movie);
        textView = findViewById(R.id.tvText);
        btnSong = findViewById(R.id.btnSong);
    }
  public BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
          String name = intent.getStringExtra(MusicKey);
          Log.d(TAG, "onReceive: ");
          if(name.equals("done")){
              btnSong.setText("Play");
          }
      }
  };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,BoundService.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadCastReceiver,new IntentFilter(BoundService.MusicService));

    }

    public void onButtonClicked(View view) {
        Log.d(TAG, "onButtonClicked: " + mBound);
        if(mBound){
            if(mBoundeService.isPlaying()){
                mBoundeService.pause();
                btnSong.setText("Play");
            }else{

//               Intent intent = new Intent(this, ForegroundService.class);
//              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startForegroundService(intent);
//                } else {
//                    startService(intent);
//                }
                mBoundeService.play();
                Intent intent = new Intent(this,BoundService.class);
                intent.setAction(Constants.Music_Start);
                startService(intent);
                btnSong.setText("Pause");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(serviceConnection);
            mBound = false;
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadCastReceiver);
    }
}