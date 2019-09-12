package com.samahmakki.companion;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Locale;
<<<<<<< Updated upstream
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
=======
>>>>>>> Stashed changes

public class MedicationActivity extends AppCompatActivity {
    DatePicker datePicker;

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

    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        datePicker = findViewById(R.id.datePicker);
<<<<<<< Updated upstream
=======
        datePicker.setFirstDayOfWeek(Calendar.SATURDAY);
>>>>>>> Stashed changes

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
<<<<<<< Updated upstream
        /*DatePickerDialog dialog = new DatePickerDialog(Context);
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialog.setTitle(R.string.activities);
        dialog.show();
        */
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
                        mGeneratedDateIcon = mImageGenerator.generateDateImage(mCurrentDate, R.drawable.ic_launcher);
                        mDisplayGenerateImage.setImageBitmap(mGeneratedDateIcon);
                    }
                }, year, month, day);
                mDatePicker.show();


            }
        });

        }


=======
        DatePickerDialog dialog = new DatePickerDialog();
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialog.setTitle(R.string.activities);
        dialog.show();
    }
>>>>>>> Stashed changes
}
