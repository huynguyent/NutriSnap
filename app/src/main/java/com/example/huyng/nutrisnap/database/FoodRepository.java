package com.example.huyng.nutrisnap.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FoodRepository {
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase writeableDatabase;
    private SQLiteDatabase readableDatabase;

    public FoodRepository(Context context) {
        mDbHelper = new DatabaseHelper(context);
        writeableDatabase = mDbHelper.getWritableDatabase();
        readableDatabase = mDbHelper.getReadableDatabase();
    }

    public Food findFoodByCode(String code) {
        Food food = null;
        String[] projection = {
                Food.COLUMN_FOOD_CODE,
                Food.COLUMN_FOOD_NAME,
                Food.COLUMN_UNIT,
                Food.COLUMN_SERVING,
                Food.COLUMN_CALORIES,
                Food.COLUMN_PROTEIN,
                Food.COLUMN_FAT,
                Food.COLUMN_CARB,
        };
        String selection = Food.COLUMN_FOOD_CODE + " = ?";
        String[] selectionArgs = { code };
        Cursor cursor = readableDatabase.query(Food.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToNext()) {
            food = new Food(
                    cursor.getString(cursor.getColumnIndexOrThrow(Food.COLUMN_FOOD_CODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Food.COLUMN_FOOD_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Food.COLUMN_UNIT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Food.COLUMN_SERVING)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Food.COLUMN_CALORIES)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(Food.COLUMN_PROTEIN)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(Food.COLUMN_FAT)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(Food.COLUMN_CARB))
                );
        }
        cursor.close();
        return food;
    }
}
