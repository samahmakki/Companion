package com.samahmakki.companion.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.InterstitialAd;
import com.samahmakki.companion.R;

import java.util.Calendar;

public class CurrentBillsFragment extends Fragment {
    ListView currentListView;
    View emptyView;
    SQLiteDatabase db;

    String updateGoal = "0";
    String updateTask = "0";
    String currentDate;
    String storeDate;
    int selectedItem;
    int countedData = 0;
    int goalActivityNumber;
    private InterstitialAd mInterstitial;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


return currentListView;


    }}


