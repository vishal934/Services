package com.example.myapplication;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyDownloadJob extends JobService {
    private boolean isJobCancelled =false;
    private boolean mSuccess =false;
    public MyDownloadJob() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("MTAG","OnStartJob: Thread name:"+Thread.currentThread().getName());

        new Thread(new Runnable() {
            @Override
            public void run() {
               int i =0;
                Log.d("MTAG","Download Started");
               while(i<10){
                   if(isJobCancelled)
                       return;
                   Log.d("MTAG","Download Progress"+(i+1));
                   try{
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   i++;
               }
                Log.d("MTAG","Download Completed");
               mSuccess=true;
                jobFinished(params,mSuccess);
            }
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        isJobCancelled = true;
        return true;
    }


}