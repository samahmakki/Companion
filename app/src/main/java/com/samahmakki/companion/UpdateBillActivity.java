package com.samahmakki.companion;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.samahmakki.companion.data.BillDbHelper;

import java.util.Calendar;

public class UpdateBillActivity extends AppCompatActivity {
    String[] bills = {"فاتورة كهرباء", "فاتورة مياه", "فاتورة غاز", "فاتورة انترنت", "فاتورة تليفون"};

    AutoCompleteTextView billAutoCompleteTextView;
    String completeText;
    TextView reminderTimesTextView, reminderDateTextView;
    Calendar mCurrentTime;
    Calendar mCurrentDate;
    DatePickerDialog mPickerDialog;
    int year, month, day, hour, minute;
    String startDate;
    String startTime;

    String startDate2;
    String startTime2;

    String mReminderDays;
    int sReminderDays;
    RadioGroup radioGroup;
    RadioButton monthlyRadioButton, weeklyRadioButton;
    private Button saveButton, deleteButton;
    BillDbHelper mBillHelper;
    private String selectedName;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bill);

        billAutoCompleteTextView = findViewById(R.id.bill_autocomplete);
        reminderTimesTextView = findViewById(R.id.reminder_times);
        reminderDateTextView = findViewById(R.id.reminder_date);
        radioGroup = findViewById(R.id.radio_group);
        monthlyRadioButton = findViewById(R.id.monthly);
        weeklyRadioButton = findViewById(R.id.weekly);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);


        mBillHelper = new BillDbHelper(this);

        Intent receivedIntent = getIntent();
        selectedID = receivedIntent.getIntExtra("id", -1);
        selectedName = receivedIntent.getStringExtra("name");
        startTime = receivedIntent.getStringExtra("time");
        startDate = receivedIntent.getStringExtra("date");

        billAutoCompleteTextView.setText(selectedName);
          /*
        AutoCompleteText for bills
         */
        //Creating the instance of ArrayAdapter containing list of bills names
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateBillActivity.this
                , android.R.layout.select_dialog_item, bills);
        //Getting the instance of AutoCompleteTextView
        billAutoCompleteTextView = findViewById(R.id.bill_autocomplete);
        billAutoCompleteTextView.setThreshold(1);//will start working from first character
        billAutoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


                 /*
        Reminder times
         */
        reminderTimesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentTime = Calendar.getInstance();
                hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateBillActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime2 = con(selectedHour) + ":" + con(selectedMinute);
                        reminderTimesTextView.setText(startTime2);
                        // alarmStartTime = mCurrentTime.getTimeInMillis();
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


                /*
        Reminder date
         */
        reminderDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentDate = Calendar.getInstance();
                year = mCurrentDate.get(Calendar.YEAR);
                month = mCurrentDate.get(Calendar.MONTH);
                day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                mPickerDialog = new DatePickerDialog(UpdateBillActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                        startDate2 = Year + "-" + (Month + 1) + "-" + Day;
                        reminderDateTextView.setText(startDate2);

                        mCurrentDate.set(Year, (Month + 1), Day);
                    }
                }, year, month, day);
                mPickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mPickerDialog.setTitle("Select Date");
                mPickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeText = billAutoCompleteTextView.getText().toString().trim();
                startDate2 = reminderDateTextView.getText().toString().trim();
                startTime2 = reminderTimesTextView.getText().toString().trim();

                mBillHelper.updateName(completeText, selectedID, selectedName, startTime2, startTime, startDate2, startDate);
                // mBillHelper.updateName(completeText, selectedID, selectedName);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBillHelper.deleteName(selectedID, selectedName);
                billAutoCompleteTextView.setText("");
                Toast.makeText(getBaseContext(), "removed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public String con(int time) {
        if (time >= 10) {
            return String.valueOf(time);
        } else {
            return "0" + String.valueOf(time);
        }
    }
}
