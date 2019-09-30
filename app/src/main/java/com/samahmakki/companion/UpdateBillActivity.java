package com.samahmakki.companion;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
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

import com.samahmakki.companion.data.BillContract;
import com.samahmakki.companion.data.BillContract.BillEntry;
import com.samahmakki.companion.data.BillDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        final Intent receivedIntent = getIntent();
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
                ContentValues cv = new ContentValues();

                if (mReminderDays == "monthly") {
                    sReminderDays = 0;
                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(UpdateBillActivity.this, BillAlarmReceiver.class);

                    String alertTitle = billAutoCompleteTextView.getText().toString();
                    intent.putExtra(getString(R.string.alert_title), alertTitle);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateBillActivity.this,
                            0, intent, 0);

                    alarmMgr.set(AlarmManager.RTC_WAKEUP, mCurrentTime.getTimeInMillis(), pendingIntent);
                    cv.put(BillContract.BillEntry.COLUMN_Bill_TIME, startTime);
                    cv.put(BillEntry.COLUMN_Bill_DATE, startDate);
                }

                else if (mReminderDays == "weekly") {
                    sReminderDays = 1;

                    SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                    startTime = formatter.format(new Date(mCurrentDate.getTimeInMillis()));
                    SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
                    startDate = dateformatter.format(new Date(mCurrentDate.getTimeInMillis()));

                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(UpdateBillActivity.this, BillNotificationManager.class);
                    String alertTitle = billAutoCompleteTextView.getText().toString();
                    intent.putExtra(getString(R.string.alert_title), alertTitle);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateBillActivity.this,
                            0, intent, 0);

                    alarmMgr.set(AlarmManager.RTC_WAKEUP, mCurrentTime.getTimeInMillis(), pendingIntent);
                    cv.put(BillEntry.COLUMN_Bill_TIME, startTime);
                    cv.put(BillEntry.COLUMN_Bill_DATE, startDate);
                }
                //mBillHelper.updateName(completeText, selectedID, selectedName,startTime2, startTime, startDate2, startDate);
                mBillHelper.updateName2(completeText, selectedID, startTime2, startDate2);
                Intent intent = new Intent(UpdateBillActivity.this, BillsActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBillHelper.deleteName(selectedID, selectedName);
                billAutoCompleteTextView.setText("");
                Toast.makeText(getBaseContext(), "removed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateBillActivity.this, BillsActivity.class);
                startActivity(intent);
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
