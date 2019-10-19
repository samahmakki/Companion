package com.samahmakki.companion;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.samahmakki.companion.R;
import com.samahmakki.companion.data.BillDbHelper;
import com.samahmakki.companion.data.BillContract.BillEntry;

import java.util.ArrayList;

public class CurrentBillsFragment extends Fragment {
    ListView listView;
    View rootView;
    BillDbHelper mBillHelper;
    SQLiteDatabase db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.all_bills_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {

       /* ArrayList<String> Bills = new ArrayList<>();
        Bills.add("فاتورة كهرباء");
        Bills.add("فاتورة مياه");
        Bills.add("فاتورة غاز");
        Bills.add("فاتورة انترنت");
        Bills.add("فاتورة تليفون");
*/

        mBillHelper = new BillDbHelper(getContext());
        db = mBillHelper.getWritableDatabase();

        String[] from = {BillEntry.COLUMN_Bill_Name, BillEntry.COLUMN_Bill_DATE, BillEntry.COLUMN_Bill_TIME};
        final String[] column = {BillEntry._Id, BillEntry.COLUMN_Bill_Name, BillEntry.COLUMN_Bill_DATE, BillEntry.COLUMN_Bill_TIME};
        int[] to = {R.id.bill_name, R.id.bill_date, R.id.bill_time};

        final Cursor cursor = db.query(BillEntry.TABLE_NAME, column, null, null ,null, null, null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.bill_list_item, cursor, from, to, 0);

        listView = rootView.findViewById(R.id.bills_listView);
        listView.setAdapter(adapter);
    }
}
