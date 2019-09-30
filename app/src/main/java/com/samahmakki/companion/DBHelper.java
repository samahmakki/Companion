package com.samahmakki.companion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public void queryData(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    //insert data
    public void insertData(String name, byte[] image) {
        SQLiteDatabase db = getWritableDatabase();

        //query to insert medicine in database table
        String sql = "INSERT INTO MEDICINE VALUES(NULL,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindBlob(2, image);


        statement.executeInsert();
    }


    //update data
    public void updateData(String name, byte[] image, int id) {
        SQLiteDatabase db = getWritableDatabase();
        //query to update medicine
        String sql = "UPDATE MEDICINE SET name=?,image=? WHERE id=?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, name);
        statement.bindBlob(2, image);
        statement.bindDouble(3, (double) id);
        statement.execute();
        db.close();
    }

    //delete data
    public void deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM MEDICINE WHERE id = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double) id);
        statement.execute();
        db.close();
    }
    //get all data from database

    public Cursor getData(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE IF NOT EXISTS MEDICINE(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, image BLOB)";
        db.execSQL(query);


    }


    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }


}
