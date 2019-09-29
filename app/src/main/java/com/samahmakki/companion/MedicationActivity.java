package com.samahmakki.companion;

import android.app.DatePickerDialog;
import android.graphics.Color;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class MedicationActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.nav_today:
                    transaction.replace(R.id.fragment_container, new TodayFragment()).commit();
return true;
                case R.id.nav_addmed:
                    transaction.replace(R.id.fragment_container, new AddmedFragment()).commit();
return true;

                case R.id.nav_medbox:
                    transaction.replace(R.id.fragment_container, new KitFragment()).commit();
                    return true;
            }


            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        BottomNavigationView bottomnav = findViewById(R.id.navigation);

        bottomnav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TodayFragment()).commit();
        }


    }
}








