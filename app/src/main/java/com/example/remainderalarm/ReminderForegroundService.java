package com.example.remainderalarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderForegroundService extends Service {
    private DatabaseHelper dbHelper;
    private static final String TAG = "ReminderForegroundService";
    private static final String CHANNEL_ID = "ReminderChannel";
    private static final long DELAY_MS = 2 * 60 * 1000;
    private long lastNotificationTime = 0;
    private Handler handler = new Handler();
//    private Runnable backgroundRunnable;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run Thread: " + Thread.currentThread().getName());
            showNotification();
            Logger.log(getApplicationContext(), "This is log");
//            fetchDataFromApi();
            dbHelper = new DatabaseHelper(ReminderForegroundService.this);
            someMethod();
//            backgroundRunnable();
//            postDataToApi();
        }
        public void someMethod() {
            // Create and start the background thread
            BackgroundThread backgroundThread = new BackgroundThread(ReminderForegroundService.this);
            backgroundThread.start();
        }

//        private void backgroundRunnable() {
//            backgroundRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    Log.i(TAG, "Background Thread: " + Thread.currentThread().getName());
//                    postDataToApi();
//                    handler.postDelayed(this, DELAY_MS);
//                }
//            };
//            Thread thread = new Thread(backgroundRunnable);
//            thread.start();
//        }
    };

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler(Looper.getMainLooper());
        Log.i(TAG, "onStartCommand: Service started:  " + Thread.currentThread().getName());
        createNotificationChannel();
        startForeground(1, buildNotification());
        handler.post(runnable);

        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e(TAG, "createNotificationChannel: Creating notification channel");
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private Notification buildNotification() {
        Log.e(TAG, "buildNotification: Building notification");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Display Log in every 2 minute")
                .setContentText("Current Log: " + getCurrentDateTime())
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentIntent(pendingIntent)
                .build();

    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showNotification() {
        Log.i(TAG, "showNotification: " + Thread.currentThread().getName());
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        Notification notification = buildNotification();
        notificationManager.notify(1, notification);
        scheduleNotification();
    }

    private void scheduleNotification() {
        handler.postDelayed(runnable, DELAY_MS);
//        handler.postDelayed(backgroundRunnable, DELAY_MS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: Service destroyed by Stopping background thread");
        handler.removeCallbacks(runnable);
//        handler.removeCallbacks(backgroundRunnable);
    }

//    private void fetchDataFromApi() {
//        // Make API call using Retrofit
//        retrofit2.Call<List<Post>> call = Retrofit.getInstance().getApiService().getData();
//        call.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(retrofit2.Call<List<Post>> call, Response<List<Post>> response) {
//                if (!response.isSuccessful()) {
//                    Toast.makeText(ReminderForegroundService.this, response.code(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (response.code() == 404) {
//                    Toast.makeText(ReminderForegroundService.this, "Page not found", Toast.LENGTH_SHORT).show();
//                }
//                Logger.log(ReminderForegroundService.this, "Api call successfully : response Code : " + response.code());
//
//                List<Post> data = response.body();
//                if (data != null) {
//                    for (Post post :
//                            data) {
//                        Log.i(TAG, "onResponse: ID: " + post.getId() + " UserID: " + post.getUserId() + " Title: " + post.getTitle());
//                        boolean isInserted = dbHelper.insertData(post.getId(), post.getUserId(), post.getTitle());
//
//                        if (isInserted) {
//                            Logger.log(ReminderForegroundService.this, "Data inserted Successfully ");
////                            Toast.makeText(getApplicationContext(), "Data inserted into SQLite", Toast.LENGTH_SHORT).show();
//                            Log.e(TAG, "Data Inserted in SQLiteDB ");
//                        } else {
//                            Logger.log(ReminderForegroundService.this, "Data Failed to insert");
////                            Toast.makeText(getApplicationContext(), "Failed to insert data into SQLite", Toast.LENGTH_SHORT).show();
//                            Log.e(TAG, "Fail to insert data in SQLiteDB ");
//                        }
//                    }
//                }
//                Gson gson = new Gson();
//                String json = gson.toJson(data);
//
//                Intent intent = new Intent(ReminderForegroundService.this, MainActivity.class);
//                intent.putExtra("postList", json);
//
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<List<Post>> call, Throwable t) {
//                Toast.makeText(ReminderForegroundService.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                Logger.log(ReminderForegroundService.this, "Api call with : error Code : " + t.getMessage());
//            }
//        });
//    }

    public void postDataToApi() {
        Log.i(TAG, "Posting data to API in background thread: " + Thread.currentThread().getName());
        List<Post> postData = new ArrayList<>();
        String localDateTime = LocalDateTime.now().toString();
        for (int i = 0; i < 20; i++) {
            int userId = 100 + i; // Example hardcoded userId
            int id = 200 + i; // Example hardcoded id
            String title = "Hardcoded Title " + (i + 1);

            postData.add(new Post(userId, id, title, null));
        }

        Call<Post> call = Retrofit.getInstance().getApiService().createData(postData);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post postResponse = response.body();
                    if (postResponse != null) {
                        Log.e(TAG, "POST request successful: " + postResponse);
                        Logger.log(ReminderForegroundService.this, "Post API call successfully: ");
                    } else {
                        Log.e(TAG, "Post request not successful ");
                    }
                    assert response.body() != null;

                    saveReminderToDatabase(localDateTime, "success");
                    boolean insertResult = dbHelper.insertData(response.body().getId(), response.body().getUserId(), response.body().getTitle());

                    if (!insertResult) {
                        Log.e(TAG, "Failed to insert data into SQLite database");
                        Logger.log(ReminderForegroundService.this, "Post Api Data Failed to insert");
                    } else {
                        Log.e(TAG, "Data inserted into SQLite database: userId= " + response.body().getUserId() + ", id= " + response.body().getId() + ", title= " + response.body().getTitle());
                        Logger.log(ReminderForegroundService.this, "Post Api data inserted successfully ");
                    }
                } else {
                    Log.e(TAG, "POST API request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e(TAG, "POST request failed: " + t.getMessage());
                String status = "failed";
                saveReminderToDatabase(localDateTime, status);
                Logger.log(ReminderForegroundService.this, "Post Api call with : error Code : " + t.getMessage());
            }
        });

    }

    private void saveReminderToDatabase(String postTime, String status) {
        // Create a new tableDataModel object
        TableDataModel tableDataModel = new TableDataModel();
        tableDataModel.setPostTime(postTime);
        tableDataModel.setStatus(status);

        // Add the tableDataModel to the database
        dbHelper.addReminder(tableDataModel);
    }
}