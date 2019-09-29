package com.samahmakki.companion;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.samahmakki.companion.data.BillContract;
import com.samahmakki.companion.data.BillContract.BillEntry;

/**
 * We using CursorAdapter since it's better than the ArrayAdapter.
 * The CursorAdapter have Two Implements method:
 * bindView: get the data and display it on the screen.
 * newView: inflate the xml Layout to display the data on ot.
 */
public class BillAdapter2 extends CursorAdapter {

    //Constructor
    public BillAdapter2(Context context, Cursor c) {
        super(context, c, 0);
    }

    //inflate the xml Layout to display the data on ot.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.bill_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView billName = view.findViewById(R.id.bill_name);
        billName.setText(cursor.getString(cursor.getColumnIndex(BillEntry.COLUMN_Bill_Name)));

        TextView billDate = view.findViewById(R.id.bill_date);
        billDate.setText(cursor.getString(cursor.getColumnIndex(BillEntry.COLUMN_Bill_DATE)));

        TextView billTime = view.findViewById(R.id.bill_time);
        billTime.setText(cursor.getString(cursor.getColumnIndex(BillEntry.COLUMN_Bill_TIME)));
    }
}
