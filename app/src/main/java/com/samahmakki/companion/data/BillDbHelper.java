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
    private static final String DATABASE_NAME = "bills.db";
    //DataBase Version
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase db;

    //Create Bill Table Query
    String SQL_CREATE_BILLS_TABLE = "CREATE TABLE " + BillEntry.TABLE_NAME + " ("
            + BillEntry._Id + " INTEGER  PRIMARY KEY  AUTOINCREMENT , "
            + BillEntry.COLUMN_Bill_Name + " TEXT NOT NULL, "
            + BillEntry.COLUMN_Bill_TIME + " TIME NOT NULL, "
            + BillEntry.COLUMN_Bill_DATE + " DATE NOT NULL, "
            + BillEntry.COLUMN_Bill_REMINDER_DAYS + " INTEGER NOT NULL);";

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
    final public long insertBill (String billName, String time, String date, int reminderDays) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BillEntry.COLUMN_Bill_Name, billName);
        values.put(BillEntry.COLUMN_Bill_TIME, time);
        values.put(BillEntry.COLUMN_Bill_DATE, date);
        values.put(BillEntry.COLUMN_Bill_REMINDER_DAYS, reminderDays);

        long newRowId = db.insert(BillEntry.TABLE_NAME, null, values);

        db.close();
        return newRowId;

        /* AllBillsFragment nextFrag = new AllBillsFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragments_container, nextFrag, "findThisFragment")
                .addToBackStack(null).commit();
                */
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
    //get all data from database

    public Cursor getData() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(
                BillEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }


    public void deleteBill(long id) {
        db = this.getWritableDatabase();
        db.delete(BillEntry.TABLE_NAME, BillEntry._Id + " = ?",
                new String[]{String.valueOf((id))});
        db.close();
    }
}
