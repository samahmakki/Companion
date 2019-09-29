package com.samahmakki.companion;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.samahmakki.companion.R;
import com.samahmakki.companion.data.BillContract;
import com.samahmakki.companion.data.BillContract.BillEntry;
import com.samahmakki.companion.data.BillDbHelper;

import java.util.ArrayList;
import java.util.Objects;

public class AllBillsFragment extends Fragment {

    ArrayList<Bill> BillsItem;
    ListView listView;
    View rootView;
    BillDbHelper mBillHelper;
    BillAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.all_bills_fragment, container, false);
        readBill();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBillHelper = new BillDbHelper(rootView.getContext());
                Bill bill = BillsItem.get(position);
                String name = bill.getBillName();
                String time = bill.getBillTime();
                String date = bill.getBillDate();

                Cursor data = mBillHelper.getItemID2(name, time, date);
                int itemID = -1;
                String itemName;
                String itemTime;
                String itemDate;
                while (data.moveToNext()){
                    itemID = data.getInt(0);

                }
                if (itemID > -1){
                    Intent updateActivity = new Intent(getContext(), UpdateBillActivity.class);
                    updateActivity.putExtra("id", itemID);
                    updateActivity.putExtra("name", name);
                    updateActivity.putExtra("time", time);
                    updateActivity.putExtra("date", date);
                    startActivity(updateActivity);
                }
            }
        });
        return rootView;
    }

    private void readBill() {

        BillDbHelper mBillHelper = new BillDbHelper(rootView.getContext());

        SQLiteDatabase db = mBillHelper.getReadableDatabase();
        String[] projection = {
                BillEntry.COLUMN_Bill_Name,
                BillEntry.COLUMN_Bill_DATE,
                BillEntry.COLUMN_Bill_TIME
        };

        Cursor cursor = db.query(
                BillEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        BillsItem = new ArrayList<Bill>();

        adapter = new BillAdapter(rootView.getContext(), BillsItem);
        listView = rootView.findViewById(R.id.bills_listView);
        listView.setAdapter(adapter);


        try {
            // Figure out the index of each column
            int nameColumnIndex = cursor.getColumnIndex(BillContract.BillEntry.COLUMN_Bill_Name);
            int dateColumnIndex = cursor.getColumnIndex(BillEntry.COLUMN_Bill_DATE);
            int timeColumnIndex = cursor.getColumnIndex(BillEntry.COLUMN_Bill_TIME);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentName = cursor.getString(nameColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                String currentTime = cursor.getString(timeColumnIndex);

                BillsItem.add(new Bill(currentName, currentDate, currentTime));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
}
