package com.samahmakki.companion;

import android.app.DatePickerDialog;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Objects;

public class TodayFragment extends Fragment {

    TextView tvdate;

    FloatingActionButton mFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(getString(R.string.medication_for_today));

        tvdate = view.findViewById(R.id.tv_date);

        mFab = view.findViewById(R.id.fab_addmed);

        //floating button to add new medicine
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addmedFragment = new AddmedFragment();
                FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                transaction.replace(R.id.fragment_container, addmedFragment);
                // if written, this transaction will be added to backstack
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        Calendar calendar1 = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar1.getTime());

        tvdate.setText(currentDate);






        return view;
    }




    }



