package com.example.aplikasisqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.aplikasisqlite.model.Data;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "vsga.db";
    public static final String TABLE_SQLITE = "sqlite";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";

    public DbHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE = "CREATE TABLE " + TABLE_SQLITE + "("+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_ADDRESS + " TEXT NOT NULL)";
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_SQLITE;
        db.execSQL(SQL_DROP);
        onCreate(db);
    }

    public ArrayList<HashMap<String,String>> getAllData(){
        ArrayList<HashMap<String,String>> wordList;
        wordList = new ArrayList<HashMap<String,String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLITE;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            do{
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_NAME, cursor.getString(1));
                map.put(COLUMN_ADDRESS, cursor.getString(2));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        Log.e("SELECT SQLITE", "" + wordList);
        database.close();
        return wordList;
    }

    public boolean isIdExists(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT 1 FROM " + TABLE_SQLITE + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        database.close();
        return exists;
    }

    public void insert(String name, String address) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_SQLITE + " (name, address) VALUES ('" + name + "', '" + address + "')";
        Log.e("insert sqlite", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void update(int id, String name, String address) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_ADDRESS, address);

        int result = database.update(TABLE_SQLITE, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        if(result > 0) {
            Log.e("update sqlite", "Update successful for ID: " + id);
        } else {
            Log.e("update sqlite", "Update failed for ID: " + id);
        }

        database.close();
    }


    public void delete(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        int result = database.delete(TABLE_SQLITE, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        if(result > 0) {
            Log.e("delete sqlite", "Delete successful for ID: " + id);
        } else {
            Log.e("delete sqlite", "Delete failed for ID: " + id);
        }

        database.close();
    }

}