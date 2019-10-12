package com.samahmakki.companion.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.samahmakki.companion.Bill;
import com.samahmakki.companion.data.BillContract.BillEntry;

public class BillDbHelper extends SQLiteOpenHelper {
    //DataBase Name
    private static final String DATABASE_NAME = "BILLS";
    //DataBase Version
    private static final int DATABASE_VERSION = 7;

    SQLiteDatabase db;

    //Create Bill Table Query
    String SQL_CREATE_BILLS_TABLE = "CREATE TABLE " + BillEntry.TABLE_NAME + " ("
            + BillEntry._Id + " INTEGER  PRIMARY KEY  AUTOINCREMENT , "
            + BillEntry.COLUMN_Bill_Name + " TEXT NOT NULL, "
            + BillEntry.COLUMN_Bill_TIME + " TEXT NOT NULL, "
            + BillEntry.COLUMN_Bill_DATE + " TEXT NOT NULL);";

    //Constructor
    public BillDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BILLS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + BillEntry.TABLE_NAME);
        onCreate(db);
    }

    //insert data into the Bills Table
    final public long insertBill (String billName, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BillEntry.COLUMN_Bill_Name, billName);
        values.put(BillEntry.COLUMN_Bill_TIME, time);
        values.put(BillEntry.COLUMN_Bill_DATE, date);

        long newRowId = db.insert(BillEntry.TABLE_NAME, null, values);

        db.close();
        return newRowId;

        /* AllBillsFragment nextFrag = new AllBillsFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragments_container, nextFrag, "findThisFragment")
                .addToBackStack(null).commit();
                */
    }

    //insert data into the Bills Table
    final public long insertBillReminder (String billName, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BillEntry.COLUMN_Bill_Name, billName);
        values.put(BillEntry.COLUMN_Bill_TIME, time);
        values.put(BillEntry.COLUMN_Bill_DATE, date);

        long newRowId = db.insert(BillEntry.TABLE_NAME, null, values);

        db.close();
        return newRowId;
    }

    final public void insert ( String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BillEntry.COLUMN_Bill_TIME, time);
        values.put(BillEntry.COLUMN_Bill_DATE, date);

        db.close();
    }

    public void updateName2(String newName, int id, String newTime, String newDate){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + BillEntry.TABLE_NAME + " SET " + BillEntry.COLUMN_Bill_Name +
                " = '" + newName +  "' , " + BillEntry.COLUMN_Bill_TIME + " = '" + newTime + "'" +
                " , " + BillEntry.COLUMN_Bill_DATE + " = '" + newDate + "'" +
                " WHERE " + BillEntry._Id + " = '" + id + "'";
        db.execSQL(query);
    }
    public void updateName(String newName, int id, String oldName, String newTime, String oldTime, String newDate, String oldDate){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + BillEntry.TABLE_NAME + " SET " + BillEntry.COLUMN_Bill_Name +
                " = '" + newName +  "' , " + BillEntry.COLUMN_Bill_TIME + " = '" + newTime + "'" +
                " , " + BillEntry.COLUMN_Bill_DATE + " = '" + newDate + "'" +
                " WHERE " + BillEntry._Id + " = '" + id + "'" +
                " AND " + BillEntry.COLUMN_Bill_Name + " = '" + oldName + "'" +
                " AND " + BillEntry.COLUMN_Bill_TIME + " = '" + oldTime + "'" +
                " AND " + BillEntry.COLUMN_Bill_DATE + " = '" + oldDate + "'";
        db.execSQL(query);
    }


    //delete data
    public void deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM "+ DATABASE_NAME +" WHERE id = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.execute();
        db.close();
    }

    public Cursor getData(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    //get all data from database

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + BillEntry.TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + BillEntry._Id + " FROM " + BillEntry.TABLE_NAME +
                " WHERE " + BillEntry.COLUMN_Bill_Name + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemID2(String name, String time, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + BillEntry._Id + " FROM " + BillEntry.TABLE_NAME +
                " WHERE " + BillEntry.COLUMN_Bill_Name + " = '" + name + "'" +
                " AND " + BillEntry.COLUMN_Bill_TIME + " = '" + time + "'" +
                " AND " + BillEntry.COLUMN_Bill_DATE + " = '" + date + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + BillEntry.TABLE_NAME + " SET " + BillEntry.COLUMN_Bill_Name +
                " = '" + newName + "' WHERE " + BillEntry._Id + " = '" + id + "'" +
                " AND " + BillEntry.COLUMN_Bill_Name + " = '" + oldName + "'";
        db.execSQL(query);
    }


    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + BillEntry.TABLE_NAME + " WHERE " +
                BillEntry._Id + " = '" + id + "'" + " AND " + BillEntry.COLUMN_Bill_Name +
                " = '" + name + "'";
        db.execSQL(query);
    }


    public void deleteBill(long id) {
        db = this.getWritableDatabase();
        db.delete(BillEntry.TABLE_NAME, BillEntry._Id + " = ?",
                new String[]{String.valueOf((id))});
        db.close();
    }
}
