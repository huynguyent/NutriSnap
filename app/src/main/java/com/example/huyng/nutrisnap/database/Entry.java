package com.example.huyng.nutrisnap.database;


import android.provider.BaseColumns;


public class Entry implements BaseColumns {
    public static final String TABLE_NAME = "entry";
    public static final String COLUMN_FOOD_CODE = "food_code";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_IMAGE = "image";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_FOOD_CODE + " TEXT," +
                    COLUMN_TIME + " TEXT," +
                    COLUMN_AMOUNT + " INTEGER," +
                    COLUMN_IMAGE + " TEXT)";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private String foodCode;
    private String time;
    private int amount;
    private String image;

    public Entry(String foodCode, String time, int amount, String image) {
        this.foodCode = foodCode;
        this.time = time;
        this.amount = amount;
        this.image = image;
    }

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
