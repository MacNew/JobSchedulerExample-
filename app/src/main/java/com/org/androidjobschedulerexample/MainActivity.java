package com.org.androidjobschedulerexample;

import android.app.job.JobInfo;
import android.content.ComponentName;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    public static final int JOB_ID = 123;

    // This anotation is required because my min target api is 19
    // min api level should be 21, that is LOLLIPOP
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ComponentName componentName = new ComponentName(this, JobScheduler.class);

        JobInfo info = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                // This is because our job is true after booting a system
                // For this we need to provide permission on Manifest, please look my Manifest
                .setPersisted(true)
                // We are executing this method for 15 min
                .setPeriodic(15 * 60 * 1000).build();

         android.app.job.JobScheduler scheduler = (android.app.job.JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

         int resultCode = scheduler.schedule(info);

         if (resultCode == android.app.job.JobScheduler.RESULT_SUCCESS) {
             Log.d(TAG, "Job Scheduled ");
             // which mean sucussfully Scheduler our job
         } else {
             // System rejected our job
             Log.d(TAG, "Job Scheduling Faild");
         }
    }


     // How we can cancle our job

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob() {
        android.app.job.JobScheduler scheduler = (android.app.job.JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        scheduler.cancel(JOB_ID);
    }

 /*we need to register our JobScheduler class on Manifest class
     and provide some permission like this
      <service android:name=".JobScheduler"
    android:permission="android.permission.BIND_JOB_SERVICE"/>
            />
*/

}
