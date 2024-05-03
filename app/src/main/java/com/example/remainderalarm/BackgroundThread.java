package com.example.remainderalarm;

import android.os.Handler;
import android.util.Log;

import java.util.logging.LogRecord;

public class BackgroundThread extends Thread{
    private ReminderForegroundService service;
    private android.os.Handler handler = new Handler();
    private static final String TAG = "BackgroundThread";

    public BackgroundThread(ReminderForegroundService service) {
        this.service = service;
    }
    @Override
    public void run() {
        Log.i(TAG, "Background Thread: " + Thread.currentThread().getName());
        // Perform background operation here
        service.postDataToApi(); // Call the method from RemainderForegroundService
//        handler.postDelayed(this, 2*60*1000);
    }
};
