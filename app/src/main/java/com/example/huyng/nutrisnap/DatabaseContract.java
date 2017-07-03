package com.example.huyng.nutrisnap;

import android.provider.BaseColumns;

public final class DatabaseContract {

    private DatabaseContract(){}

    // Database information
    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "database.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    // Database tables
    public static class Food implements BaseColumns {
        public static final String TABLE_NAME = "food";
        public static final String FOOD_NAME = "food_name";
        public static final String CALORIES = "calories";
        public static final String PROTEIN = "protein";
        public static final String FAT = "fat";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        FOOD_NAME + " TEXT," +
                        CALORIES + " REAL," +
                        PROTEIN + " REAL," +
                        FAT + " REAL)";
        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    }
}
