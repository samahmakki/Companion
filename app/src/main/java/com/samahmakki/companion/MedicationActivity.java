package com.samahmakki.companion;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Locale;

public class MedicationActivity extends AppCompatActivity {

    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        datePicker = findViewById(R.id.datePicker);

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        /*DatePickerDialog dialog = new DatePickerDialog(Context);
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialog.setTitle(R.string.activities);
        dialog.show();
        */
    }
}
