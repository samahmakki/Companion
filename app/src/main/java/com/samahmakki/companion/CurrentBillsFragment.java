package com.samahmakki.companion;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.samahmakki.companion.R;
import com.samahmakki.companion.data.BillDbHelper;

public class CurrentBillsFragment extends Fragment {
    ListView currentListView;
    View emptyView;

    String updateBill = "0";
    String currentDate;
    String storeDate;
    int selectedItem;
    int countedData = 0;

    SQLiteDatabase db;
    BillDbHelper mDBHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.current_bills_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {
        currentListView = rootView.findViewById(R.id.current_goal_list);
        emptyView = rootView.findViewById(R.id.empty_view);

        mDBHelper = new BillDbHelper(rootView.getContext());

        }
}
