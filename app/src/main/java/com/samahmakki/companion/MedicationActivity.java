package com.samahmakki.companion;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Calendar;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kd.dynamic.calendar.generator.ImageGenerator;

import java.time.Year;

public class MedicationActivity extends AppCompatActivity {

    EditText mDateET;
    Calendar mCurrentDate;
    Bitmap mGeneratedDateIcon;
    ImageView mDisplayGenerateImage;
    ImageGenerator mImageGenerator;
    private BottomNavigationView.OnNavigationItemReselectedListener navListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_today:
                    selectedFragment = new TodayFragment();
                    break;

                case R.id.nav_addmed:
                    selectedFragment = new AddmedFragment();
                    break;

                case R.id.nav_medbox:
                    selectedFragment = new KitFragment();
                    break;
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        BottomNavigationView bottomnav = findViewById(R.id.navigation);

        bottomnav.setOnNavigationItemReselectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodayFragment()).commit();


        mImageGenerator = new ImageGenerator(this);
        mDateET = findViewById(R.id.txtDate);
        mDisplayGenerateImage = findViewById(R.id.imageGen);
        mImageGenerator.setDateSize(30);
        mImageGenerator.setIconSize(50, 50);
        mImageGenerator.setMonthSize(10);
        mImageGenerator.setDatePosition(42);
        mImageGenerator.setMonthPosition(14);
        mImageGenerator.setDateColor(Color.parseColor("#3c6aaf"));
        mImageGenerator.setMonthColor(Color.WHITE);
        mImageGenerator.setStorageToSDCard(true);
        mDateET.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(MedicationActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedDay, int selectedMonth, int selectedYear) {
                        mDateET.setText(selectedDay + "-" + selectedMonth + "-" + selectedYear);
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);
                        mGeneratedDateIcon = mImageGenerator.generateDateImage(mCurrentDate, R.drawable.cale);
                        mDisplayGenerateImage.setImageBitmap(mGeneratedDateIcon);
                    }
                }, year, month, day);
                mDatePicker.show();


            }
        });


    }


}
