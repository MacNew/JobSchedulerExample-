package com.org.androidjobschedulerexample;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class JobScheduler extends JobService {
    private static final String TAG = "JobScheduler";
    public static boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");

        // Params are the job params that we get from main activity.

        doBackgroundWork(params);

        /* If our task is short and execute in this method and if
         this method is over  and our job is over, we have to return false;

        If our task is long and we need to do some background process
         and our job is not over after executing this method than we need to return true;*/

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {

        /* we are creating new Thread because we are assuming
         we are doing network operation like location featching */

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i= 0;i<10;i++) {

                    if (jobCancelled) {
                        return;
                    }

                    Log.d(TAG, "run: "+i);


                    // Stop our thread for 1 second
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "Job finised");
                // if we want to say system to rescheduler for this job than we need to pass true
                // if not than we need to pass false
                jobFinished(params, true);
            }
        }).start();

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // This method will be invoke if our system cancel our job
        // For example we only need to start our job only we have wify connection or any type of Network connection
        // or some thing we define in params

        // System will invoke this method not we
        Log.d(TAG, "Job cancelled before completion");

        // this boolean indicate if our job re scheduler or not
        // if we want to retry latter than return true
        jobCancelled = true;
        return true;
    }
}
