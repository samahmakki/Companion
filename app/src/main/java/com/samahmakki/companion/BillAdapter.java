package com.samahmakki.companion;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BillAdapter extends ArrayAdapter<Bill> {

    public BillAdapter(Context context, ArrayList<Bill> bills) {
        super(context, 0, bills);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.bill_list_item, parent, false);
        }

        Bill currentBill = getItem(position);

        TextView billName = listItemView.findViewById(R.id.bill_name);
        billName.setText(currentBill.getBillName());

        TextView billDate = listItemView.findViewById(R.id.bill_date);
        billDate.setText(currentBill.getBillDate());

        TextView billTime = listItemView.findViewById(R.id.bill_time);
        billTime.setText(currentBill.getBillTime());

        return listItemView;

    }

}
