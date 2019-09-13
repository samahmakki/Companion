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
        loadLocale();//load language setting

        View rootView = inflater.inflate(R.layout.current_goals_fragment, container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {
        currentListView = rootView.findViewById(R.id.current_goal_list);
        emptyView = rootView.findViewById(R.id.empty_view);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        month = month + 1;

        currentDate = year + "-" + month + "-" + day;

        currentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, final long id) {

                final PopupMenu popupMenu = new PopupMenu(rootView.getContext(), view);
                popupMenu.inflate(R.menu.pop_up_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        selectedItem = item.getItemId();
                        if (selectedItem == R.id.update) {
                            updateGoal = "1";
                            updateTask = "1";
                            goalActivityNumber = 1;
                            Intent intent = new Intent(rootView.getContext(), GoalActivity.class);
                            intent.putExtra("goalId", id);
                            intent.putExtra("updateGoal", updateGoal);
                            intent.putExtra("goalActivity", goalActivityNumber);
                            startActivity(intent);

                        } else if (selectedItem == R.id.delete) {
                            helper.deleteGoal(id);
                            Cursor cursor1 = updateUi();
                            adapter = new GoalAdapter(getContext(), cursor1);
                            currentListView.setAdapter(adapter);
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        currentListView.setAdapter(adapter);
    }

    }
