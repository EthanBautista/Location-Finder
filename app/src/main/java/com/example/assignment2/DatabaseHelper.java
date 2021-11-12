package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "Locations";
    private static final String COL1 = "ID";
    private static final String COL2 = "address";
    private static final String COL3 = "latitude";
    private static final String COL4 = "longitude";

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("+COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, "  + COL3 + " TEXT, "  + COL4+ " TEXT) ";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor fetch(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);

        return data;
    }
    public Cursor getCoordinates(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COL2 +"='"+address.toLowerCase()+"'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getAddress(String lat, String lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COL3 +"='"+lat+"' AND " + COL4 + "='"+ lng +"'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public long addData(String address, String lat, String lng){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, address.toLowerCase());
        contentValues.put(COL3, lat);
        contentValues.put(COL4, lng);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result;
    }

    public int update(String address, String lat, String lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, address.toLowerCase());
        contentValues.put(COL3, lat);
        contentValues.put(COL4, lng);

        int success = db.update(TABLE_NAME, contentValues, COL2+" = ?", new String[]{address.toLowerCase()});
        return success;
    }

    public void delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID=?",new String[]{id});
    }

    public int deleteGivenAddress(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        int success = db.delete(TABLE_NAME,COL2+"=?",new String[]{address.toLowerCase()});
        return success;
    }

    public int deleteGivenCoordinates(String lat, String lng){
        SQLiteDatabase db = this.getWritableDatabase();
        int success = db.delete(TABLE_NAME,COL3+"=? AND " + COL4+"=?",new String[]{lat, lng});
        return success;
    }
}
