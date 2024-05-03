package com.example.remainderalarm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
    private static final String TAG = "Logger";
//    private static final String TAG = "Logger";
//
//    public static void logDateTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        String currentDateTime = sdf.format(new Date());
//        Log.e(TAG, "Reminder Date & Time: " + currentDateTime);
//    }

    private static final String LOG_DIRECTORY = "Android/data/com.example.remainderalarm/logs";
    private static final String LOG_FILE_NAME_PREFIX = "log";
    private static final String LOG_FILE_EXTENSION = ".txt";
    private static final long MAX_LOG_FILE_SIZE_BYTES = 5L * 1024 * 1024; // 5MB

    private static final int REQUEST_CODE_PERMISSION = 1001;

    public static void log(Context context, String message) {
        Log.e(TAG, "log: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (context instanceof Activity && !checkStoragePermission(context))) {
            requestStoragePermission((Activity) context);
            return;
        }

        String logMessage = getCurrentTimeStamp() + " - " + message + "\n";
        Log.e(TAG, "logMessage: " +logMessage);

        try {
            File logDirectory = new File(context.getExternalFilesDir(null), LOG_DIRECTORY);
            Log.e(TAG, "logDirectory: " + logDirectory);
            if (!logDirectory.exists() && (!logDirectory.mkdirs())) {
                Log.e(TAG, "Error creating log directory");
                return;
            }

            File logFile = getCurrentLogFile(logDirectory);

            if (logFile.length() + logMessage.getBytes().length > MAX_LOG_FILE_SIZE_BYTES) {
                rotateLogFile(logFile, logDirectory);
                logFile = createNewLogFile(logDirectory);
            }

            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.append(logMessage);
                writer.flush();
            }

        } catch (IOException e) {
            Log.e(TAG, "Error writing to log file: " + e.getMessage());
        }
    }


    private static boolean checkStoragePermission(Context context) {
        int writePermission = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return writePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED;

    }

    private static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_CODE_PERMISSION);
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private static File getCurrentLogFile(File logDirectory) throws IOException {
        File[] logFiles = logDirectory.listFiles();
        if (logFiles != null) {
            for (File logFile : logFiles) {
                if (logFile.isFile() && logFile.getName().startsWith(LOG_FILE_NAME_PREFIX)) {
                    return logFile;
                }
            }
        }
        return createNewLogFile(logDirectory);
    }

    private static File createNewLogFile(File logDirectory) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String logFileName = LOG_FILE_NAME_PREFIX + timeStamp + LOG_FILE_EXTENSION;
        return new File(logDirectory, logFileName);
    }

    private static void rotateLogFile(File logFile, File logDirectory) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String rotatedLogFileName = logFile.getName().replace(LOG_FILE_NAME_PREFIX, LOG_FILE_NAME_PREFIX + timeStamp + "rotated");
        File rotatedLogFile = new File(logDirectory, rotatedLogFileName);
        if (logFile.renameTo(rotatedLogFile)) {
            Log.d(TAG, "Log file rotated: " + rotatedLogFile.getAbsolutePath());
        } else {
            Log.e(TAG, "Failed to rotate log file");
        }
    }

}

