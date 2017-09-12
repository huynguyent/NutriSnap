package com.example.huyng.nutrisnap.database;


import android.provider.BaseColumns;

public class Food implements BaseColumns {
    public static final String TABLE_NAME = "food";
    public static final String COLUMN_FOOD_CODE = "food_code";
    public static final String COLUMN_FOOD_NAME = "food_name";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_SERVING = "serving";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_PROTEIN = "protein";
    public static final String COLUMN_FAT = "fat";
    public static final String COLUMN_CARB = "carb";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_FOOD_CODE + " TEXT," +
                    COLUMN_FOOD_NAME + " TEXT," +
                    COLUMN_UNIT + " TEXT," +
                    COLUMN_SERVING + " INTEGER," +
                    COLUMN_CALORIES + " INTEGER," +
                    COLUMN_PROTEIN + " REAL," +
                    COLUMN_FAT + " REAL," +
                    COLUMN_CARB + " REAL)";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private String foodCode;
    private String name;
    private String unit;
    private int serving;
    private int calories;
    private double protein;
    private double fat;
    private double carb;

    public Food(String foodCode, String name, String unit, int serving, int calories, double protein, double fat, double carb) {
        this.foodCode = foodCode;
        this.name = name;
        this.unit = unit;
        this.serving = serving;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
    }

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarb() {
        return carb;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }
}