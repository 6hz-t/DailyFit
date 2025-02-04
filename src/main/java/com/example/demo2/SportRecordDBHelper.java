package com.example.demo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SportRecordDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "sport_record.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "records";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_SPORT_TYPE = "sport_type";
    public static final String COLUMN_IMAGE_PATH = "image_path";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATE + " TEXT NOT NULL, " +
            COLUMN_TIME + " INTEGER NOT NULL, " +
            COLUMN_SPORT_TYPE + " TEXT NOT NULL, " +
            COLUMN_IMAGE_PATH + " TEXT);";

    public SportRecordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
