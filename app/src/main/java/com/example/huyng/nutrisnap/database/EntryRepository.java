package com.example.huyng.nutrisnap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class EntryRepository {
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase writeableDatabase;
    private SQLiteDatabase readableDatabase;

    public EntryRepository(Context context) {
        mDbHelper = new DatabaseHelper(context);
        new GetDatabaseTask().execute();
    }

    public void addEntry(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(Entry.COLUMN_FOOD_CODE, entry.getFoodCode());
        values.put(Entry.COLUMN_TIME, entry.getTime());
        values.put(Entry.COLUMN_AMOUNT, entry.getAmount());
        values.put(Entry.COLUMN_IMAGE, entry.getImage());
        writeableDatabase.insert(Entry.TABLE_NAME, null, values);
    }

    private class GetDatabaseTask extends AsyncTask<Void, Void, Void> {
        private final String TAG = "GetDatabaseTask";

        @Override
        protected Void doInBackground(Void ... param) {
            writeableDatabase = mDbHelper.getWritableDatabase();
            readableDatabase = mDbHelper.getReadableDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, "Get databases finished");
        }

    }
}
