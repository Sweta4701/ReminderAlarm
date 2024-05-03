package com.example.remainderalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "posts.db";
    private static final String TABLE_NAME = "posts_table";
    private static final String COL_AUTO_ID = "AutoID";
    private static final String COL_ID = "ID";
    private static final String COL_USER_ID = "UserID";
    private static final String COL_TITLE = "Title";
    private static final String INTERFACE_TABLE = "interface_table";
    private static final String KEY_ID = "Srno";
    private static final String KEY_POST_TIME = "PostTime";
    private static final String KEY_STATUS = "Status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ID + " INTEGER, " +
                COL_USER_ID + " INTEGER, " +
                COL_TITLE + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + INTERFACE_TABLE + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_POST_TIME + " TEXT, " +
                KEY_STATUS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "INTERFACE_TABLE");
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(int id, int userId, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_USER_ID, userId);
        contentValues.put(COL_TITLE, title);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public void addReminder(TableDataModel reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POST_TIME, reminder.getPostTime());
        values.put(KEY_STATUS, reminder.getStatus());

        // Inserting Row
        db.insert(INTERFACE_TABLE, null, values);
        db.close();
    }

    public List<TableDataModel> getAllReminders() {
        List<TableDataModel> reminderList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + INTERFACE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase(); //getreadable use for read data from database
        Cursor cursor = db.rawQuery(selectQuery, null);// cursor use to store table and from that we can move 1 1 data

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Log.i(TAG, "condition true: ");
            do {
                TableDataModel reminder = new TableDataModel();
                reminder.setSrno(Integer.parseInt(cursor.getString(0)));
                reminder.setPostTime(cursor.getString(1));
                reminder.setStatus(cursor.getString(2));
                // Adding reminder to list
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }
        // close the cursor
        cursor.close();

        // return reminder list
        return reminderList;
    }
}
