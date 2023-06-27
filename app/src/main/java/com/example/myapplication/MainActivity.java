package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
 public static final int JOB_ID = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scheduleService(View view) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName =new ComponentName(this,MyDownloadJob.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setMinimumLatency(0)
                .setPersisted(true)
                .build();

       int result=  jobScheduler.schedule(jobInfo);
       if(result == JobScheduler.RESULT_SUCCESS)
           Log.d("MTAG","scheduleservice: job scheduule:");
       else
           Log.d("MTAG","OnStartJob: job not schedule");

    }

    public void cancelService(View view) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
        Log.d("MTAG","schedule cancel");
    }
}