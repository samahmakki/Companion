package com.samahmakki.companion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Blob;

import static com.samahmakki.companion.data.BillContract.BillEntry.TABLE_NAME;


public class DBHelper extends SQLiteOpenHelper {


    public static final String name = "name";
    public static final String time = "time";
    public static final String date = "date";
    public static final String repeat = "repeat";



    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public void queryData(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }



    //insert data
    public void insertData(String name, byte[] image,String date,String time,String repeat) {
        SQLiteDatabase db = getWritableDatabase();

        //query to insert medicine in database table
        String sql = "INSERT INTO MEDICINE VALUES(NULL,?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindBlob(2, image);
        statement.bindString(3, date);
        statement.bindString(4, time);
        statement.bindString(5, repeat);

        statement.executeInsert();
    }

    //update data
    public void updateData(String name, byte[] image, String date, String time,String repeat, int id) {
        SQLiteDatabase db = getWritableDatabase();
        //query to update medicine
        String sql = "UPDATE MEDICINE SET name=?,image=?,date=?,time=?,repeat=? WHERE id=?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, name);
        statement.bindBlob(2, image);
        statement.bindString(3, date);
        statement.bindString(4, time);
        statement.bindString(5, repeat);
        statement.bindDouble(6, (double)id);

        statement.execute();
        db.close();
    }

    //delete data
    public void deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM MEDICINE WHERE id = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1,(double)id);
        statement.execute();
        db.close();
    }
    //get all data from database

    public Cursor getData(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE IF NOT EXISTS MEDICINE(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, image BLOB,date VARCHAR,time VARCHAR,repeat VARCHAR)";
        db.execSQL(query);


    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }


    public void insertMEDTime_Date(String time, String date,String repeat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(time, time);
        values.put(date, date);
        values.put(repeat, repeat);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }
}
