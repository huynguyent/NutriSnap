package com.example.huyng.nutrisnap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntryRepository {
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase writeableDatabase;
    private SQLiteDatabase readableDatabase;

    public EntryRepository(Context context) {
        mDbHelper = new DatabaseHelper(context);
        writeableDatabase = mDbHelper.getWritableDatabase();
        readableDatabase = mDbHelper.getReadableDatabase();
    }

    public void addEntry(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(Entry.COLUMN_FOOD_CODE, entry.getFoodCode());
        values.put(Entry.COLUMN_TIME, entry.getTime());
        values.put(Entry.COLUMN_AMOUNT, entry.getAmount());
        values.put(Entry.COLUMN_IMAGE, entry.getImage());
        writeableDatabase.insert(Entry.TABLE_NAME, null, values);
    }

    public List<Entry> getAllEntry() {
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        String[] projection = {
                Entry.COLUMN_FOOD_CODE,
                Entry.COLUMN_TIME,
                Entry.COLUMN_AMOUNT,
                Entry.COLUMN_IMAGE
        };
        Cursor cursor = readableDatabase.query(Entry.TABLE_NAME, projection, null, null, null, null, null);
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Entry.COLUMN_FOOD_CODE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(Entry.COLUMN_TIME));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(Entry.COLUMN_IMAGE));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(Entry.COLUMN_AMOUNT));
            entryList.add(new Entry(name, time, amount, image));
        }
        cursor.close();

        return entryList;
    }

    public List<Entry> findEntryByDate(Date date) {
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-DD");
        String[] projection = {
                Entry.COLUMN_FOOD_CODE,
                Entry.COLUMN_TIME,
                Entry.COLUMN_AMOUNT,
                Entry.COLUMN_IMAGE
        };
        String selection = Entry.COLUMN_TIME+ " LIKE ?";
        String[] selectionArgs = { df.format(date) + "%"};
        Cursor cursor = readableDatabase.query(Entry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Entry.COLUMN_FOOD_CODE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(Entry.COLUMN_TIME));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(Entry.COLUMN_IMAGE));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(Entry.COLUMN_AMOUNT));
            entryList.add(new Entry(name, time, amount, image));
        }
        cursor.close();
        return entryList;
    }

}
