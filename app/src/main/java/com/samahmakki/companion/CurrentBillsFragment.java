package com.samahmakki.companion;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.samahmakki.companion.R;
import com.samahmakki.companion.data.BillDbHelper;

import java.util.ArrayList;

public class CurrentBillsFragment extends Fragment {
    ListView listView;
    View rootView;
    BillDbHelper mBillHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.all_bills_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {

        ArrayList<String> Bills = new ArrayList<>();
        Bills.add("فاتورة كهرباء");
        Bills.add("فاتورة مياه");
        Bills.add("فاتورة غاز");
        Bills.add("فاتورة انترنت");
        Bills.add("فاتورة تليفون");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.list_1, Bills);
        listView = rootView.findViewById(R.id.bills_listView);
        listView.setAdapter(adapter);
    }
}
