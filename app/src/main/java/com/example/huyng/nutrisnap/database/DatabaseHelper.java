package com.example.huyng.nutrisnap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Iterator;

public class DatabaseHelper extends SQLiteOpenHelper{
    // Database information
    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "nutrisnap.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";
    private final String JSON_FILE ="food_data.json";
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Food.SQL_CREATE_TABLE);
        db.execSQL(Entry.SQL_CREATE_TABLE);

        // Add intial food data from json file to database
        try {
            // Read JSON file
            InputStream is = context.getAssets().open(JSON_FILE);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer, "UTF-8");
            JSONObject jsonReader = new JSONObject(jsonString);
            Iterator<String> keys = jsonReader.keys();
            // Iterate through all the food
            //---INSIDE LOOP
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject food = jsonReader.getJSONObject(key);
                ContentValues values = new ContentValues();
                values.put(Food.COLUMN_FOOD_CODE, key);
                values.put(Food.COLUMN_FOOD_NAME, food.getString("name"));
                values.put(Food.COLUMN_UNIT, food.getString("unit"));
                values.put(Food.COLUMN_CALORIES, food.getInt("calories"));
                values.put(Food.COLUMN_PROTEIN, food.getDouble("protein"));
                values.put(Food.COLUMN_FAT, food.getDouble("fat"));
                values.put(Food.COLUMN_CARB, food.getDouble("carb"));
                long newRowId = db.insert(Food.TABLE_NAME, null, values);
                Log.d("DBOnCreate", key);

            }
            //---END LOOP
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Food.SQL_DELETE_TABLE);
        db.execSQL(Entry.SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
