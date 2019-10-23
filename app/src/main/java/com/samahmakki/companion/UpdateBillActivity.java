package com.samahmakki.companion;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UpdateBillActivity extends AppCompatActivity {
    private String[] bills;
    AutoCompleteTextView billAutoCompleteTextView;
    String completeText;
    TextView reminderTimesTextView, reminderDateTextView;
    DatePickerDialog mPickerDialog;
    int year, month, day, hour, minute;
    String startDate;
    String startTime;

    String startDate2;
    String startTime2;

    TimePickerDialog mTimePicker;
    Calendar calendar;
    RadioGroup radioGroup;
    BillDbHelper mBillHelper;
    private String selectedName;
    private int selectedID;
    int timePickHour;
    int timePickMinute;
    int datePickMonth;
    int datePickDay;
    int datePickYear;
    RadioButton alarmRadioButton, notifyRadioButton;

    int reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bill);

        setTitle(getResources().getString(R.string.update_delete));
        billAutoCompleteTextView = findViewById(R.id.bill_autocomplete);
        reminderTimesTextView = findViewById(R.id.reminder_times);
        reminderDateTextView = findViewById(R.id.reminder_date);
        Button updateButton = findViewById(R.id.update_button);
        Button deleteButton = findViewById(R.id.delete_button);

        radioGroup = findViewById(R.id.radio_group);
        alarmRadioButton = findViewById(R.id.alarm);
        notifyRadioButton = findViewById(R.id.notify);

        mBillHelper = new BillDbHelper(this);

        final Intent receivedIntent = getIntent();
        selectedID = receivedIntent.getIntExtra("id", -1);
        selectedName = receivedIntent.getStringExtra("name");
        startTime = receivedIntent.getStringExtra("time");
        startDate = receivedIntent.getStringExtra("date");
        reminder = receivedIntent.getIntExtra("reminder", 1);

        bills = new String[]{getResources().getString(R.string.electricity_bill),
                getResources().getString(R.string.water_bill),
                getResources().getString(R.string.gas_bill),
                getResources().getString(R.string.internet_bill),
                getResources().getString(R.string.telephone_bill)};

        billAutoCompleteTextView.setText(selectedName);
        reminderTimesTextView.setText(startTime);
        reminderDateTextView.setText(startDate);
        radioGroup.setSelected(true);

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.alarm) {
                    reminder = 1;
                    calendar = Calendar.getInstance();
                    calendar.clear();
                    calendar.set(Calendar.MONTH, datePickMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, datePickDay);
                    calendar.set(Calendar.YEAR, datePickYear);
                    calendar.set(Calendar.HOUR, timePickHour);
                    calendar.set(Calendar.MINUTE, timePickMinute);
                    calendar.set(Calendar.SECOND, 00);

                    SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                    startTime2 = formatter.format(new Date(calendar.getTimeInMillis()));
                    SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.dateformate));
                    startDate2 = dateFormatter.format(new Date(calendar.getTimeInMillis()));

                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Intent intent = new Intent(UpdateBillActivity.this, AlarmReceiver.class);

                    intent.putExtra(getString(R.string.alert_title), completeText);
                    intent.putExtra(getString(R.string.bill_name), completeText);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateBillActivity.this
                            , 0, intent, 0);

                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    mBillHelper.updateTime_Date(selectedID, startTime2, startDate2);
                }

                else if (checkedId == R.id.notify) {

                    Intent intent = new Intent(UpdateBillActivity.this, NotificationManager2.class);

                    intent.putExtra(getString(R.string.alert_title), "Reminder");
                    intent.putExtra(getString(R.string.bill_name), completeText);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateBillActivity.this
                            , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    reminder = 2;
                    calendar = Calendar.getInstance();
                    calendar.clear();
                    calendar.set(Calendar.MONTH, datePickMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, datePickDay);
                    calendar.set(Calendar.YEAR, datePickYear);
                    calendar.set(Calendar.HOUR, timePickHour);
                    calendar.set(Calendar.MINUTE, timePickMinute);
                    calendar.set(Calendar.SECOND, 00);


                    SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                    startTime2 = formatter.format(new Date(calendar.getTimeInMillis()));
                    SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.dateformate));
                    startDate2 = dateFormatter.format(new Date(calendar.getTimeInMillis()));

                    billAutoCompleteTextView = findViewById(R.id.bill_autocomplete);
                    completeText = billAutoCompleteTextView.getText().toString().trim();

                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    mBillHelper = new BillDbHelper(UpdateBillActivity.this);
                    mBillHelper.updateTime_Date(selectedID, startTime2, startDate2);
                }


                else if (checkedId == R.id.both){
                    reminder = 3;

                    Intent intent2 = new Intent(UpdateBillActivity.this, NotificationManager2.class);

                    intent2.putExtra(getString(R.string.alert_title), "Reminder");
                    intent2.putExtra(getString(R.string.bill_name), completeText);

                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(UpdateBillActivity.this
                            , 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    calendar = Calendar.getInstance();
                    calendar.clear();
                    calendar.set(Calendar.MONTH, datePickMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, datePickDay);
                    calendar.set(Calendar.YEAR, datePickYear);
                    calendar.set(Calendar.HOUR, timePickHour);
                    calendar.set(Calendar.MINUTE, timePickMinute);
                    calendar.set(Calendar.SECOND, 00);


                    SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                    startTime2 = formatter.format(new Date(calendar.getTimeInMillis()));
                    SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.dateformate));
                    startDate2 = dateFormatter.format(new Date(calendar.getTimeInMillis()));

                    Intent intent = new Intent(UpdateBillActivity.this, AlarmReceiver.class);

                    completeText = billAutoCompleteTextView.getText().toString().trim();

                    intent.putExtra(getString(R.string.alert_title), completeText);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateBillActivity.this
                            , 0, intent, 0);

                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    mBillHelper.updateTime_Date(selectedID, startTime2, startDate2);


                    billAutoCompleteTextView = findViewById(R.id.bill_autocomplete);
                    String alertTitle = billAutoCompleteTextView.getText().toString().trim();
                    intent.putExtra(getString(R.string.alert_title), alertTitle);

                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent2);
                    getDuration();

                    mBillHelper = new BillDbHelper(UpdateBillActivity.this);
                    mBillHelper.updateTime_Date(selectedID, startTime2, startDate2);
                }
            }
        });
                 /*
        Reminder times
         */
        reminderTimesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(UpdateBillActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime2 = con(selectedHour) + ":" + con(selectedMinute);

                       // String.format(startTime2, Locale.getAvailableLocales());

                        calendar = Calendar.getInstance();
                        calendar.clear();
                        calendar.set(Calendar.HOUR, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);

                        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                        startTime2 = formatter.format(new Time(calendar.getTimeInMillis()));

                        reminderTimesTextView.setText(startTime2);
                        timePickHour = timePicker.getCurrentHour();
                        timePickMinute = timePicker.getCurrentMinute();
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
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                mPickerDialog = new DatePickerDialog(UpdateBillActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                        startDate2 = Year + "-" + (Month + 1) + "-" + Day;

                        calendar = Calendar.getInstance();
                        calendar.clear();
                        calendar.set(Calendar.MONTH, Month);
                        calendar.set(Calendar.DAY_OF_MONTH, Day);
                        calendar.set(Calendar.YEAR, Year);

                        SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.dateformate));
                        startDate2 = dateFormatter.format(new Date(calendar.getTimeInMillis()));

                        reminderDateTextView.setText(startDate2);

                        calendar.set(Year, (Month + 1), Day);
                        datePickMonth = datePicker.getMonth();
                        datePickDay = datePicker.getDayOfMonth();
                        datePickYear = datePicker.getYear();
                    }
                }, year, month, day);
                mPickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mPickerDialog.setTitle("Select Date");
                mPickerDialog.show();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeText = billAutoCompleteTextView.getText().toString().trim();
                startDate2 = reminderDateTextView.getText().toString().trim();
                startTime2 = reminderTimesTextView.getText().toString().trim();

                mBillHelper = new BillDbHelper(UpdateBillActivity.this);
                mBillHelper.updateName(selectedID, completeText);



                mBillHelper = new BillDbHelper(UpdateBillActivity.this);
                mBillHelper.updateBill(completeText, selectedID, startTime2, startDate2);

                Intent openMainScreen = new Intent(UpdateBillActivity.this, BillsActivity.class);
                openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openMainScreen);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBillHelper.deleteName(selectedID, selectedName);
                billAutoCompleteTextView.setText("");
                Toast.makeText(getBaseContext(), getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
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

    private long getDuration(){
        // get todays date
        Calendar cal = Calendar.getInstance();
        // get current month
        int currentMonth = cal.get(Calendar.MONTH);

        // move month ahead
        currentMonth++;
        // check if has not exceeded threshold of december

        if(currentMonth > Calendar.DECEMBER){
            // alright, reset month to jan and forward year by 1 e.g fro 2013 to 2014
            currentMonth = Calendar.JANUARY;
            // Move year ahead as well
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
        }

        // reset calendar to next month
        cal.set(Calendar.MONTH, currentMonth);
        // get the maximum possible days in this month
        int maximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // set the calendar to maximum day (e.g in case of fEB 28th, or leap 29th)
        cal.set(Calendar.DAY_OF_MONTH, maximumDay);
        long thenTime = cal.getTimeInMillis(); // this is time one month ahead



        return (thenTime); // this is what you set as trigger point time i.e one month after

    }
}