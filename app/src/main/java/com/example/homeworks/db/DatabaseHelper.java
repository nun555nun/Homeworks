package com.example.homeworks.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "homework.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "homeworks";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_SUBJECT = "subject";
    public static final String COL_START = "start";
    public static final String COL_DEADLINE = "deadline";
    public static final String COL_DETAILS = "details";
    public static final String COL_IMAGE = "image";


    private static final String SQL_CREATE_TABLE_PHONE
            = "CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_TITLE + " TEXT,"
            + COL_SUBJECT + " TEXT,"
            + COL_START + " TEXT,"
            + COL_DEADLINE + " TEXT,"
            + COL_DETAILS + " TEXT,"
            + COL_IMAGE + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PHONE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
