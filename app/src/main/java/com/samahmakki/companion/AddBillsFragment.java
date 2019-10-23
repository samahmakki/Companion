package com.samahmakki.companion;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.samahmakki.companion.data.BillDbHelper;
import com.samahmakki.companion.data.BillContract.BillEntry;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddBillsFragment extends Fragment {

    private String[] bills;

    AutoCompleteTextView billAutoCompleteTextView;
    String completeText;
    TextView reminderTimesTextView, reminderDateTextView;
    Calendar calendar;
    int timePickHour;
    int timePickMinute;
    int datePickMonth;
    int datePickDay;
    int datePickYear;
    DatePickerDialog mPickerDialog;
    TimePickerDialog mTimePicker;

    int year, month, day, hour, minute;
    String startDate;
    String startTime;
    RadioGroup radioGroup;
    RadioButton alarmRadioButton, notifyRadioButton;
    private Button saveButton;
    BillDbHelper mBillHelper;
    SQLiteDatabase db;

    int reminder;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.add_bills_fragment, container, false);

        mBillHelper = new BillDbHelper(rootView.getContext());
        db = mBillHelper.getWritableDatabase();

        reminderTimesTextView = rootView.findViewById(R.id.reminder_times);
        reminderDateTextView = rootView.findViewById(R.id.reminder_date);

        radioGroup = rootView.findViewById(R.id.radio_group);
        alarmRadioButton = rootView.findViewById(R.id.alarm);
        notifyRadioButton = rootView.findViewById(R.id.notify);

        saveButton = rootView.findViewById(R.id.save_button);

        bills = new String[]{getResources().getString(R.string.electricity_bill),
                getResources().getString(R.string.water_bill),
                getResources().getString(R.string.gas_bill),
                getResources().getString(R.string.internet_bill),
                getResources().getString(R.string.telephone_bill)};
    /*
    AutoCompleteText for bills
     */
        //Creating the instance of ArrayAdapter containing list of bills names
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext()
                , android.R.layout.select_dialog_item, bills);
        //Getting the instance of AutoCompleteTextView
        billAutoCompleteTextView = rootView.findViewById(R.id.bill_autocomplete);
        billAutoCompleteTextView.setThreshold(1);//will start working from first character
        billAutoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView




         /*
        Reminder times
         */
        reminderTimesTextView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime = con(selectedHour) + ":" + con(selectedMinute);

                       // String.format(startTime, Locale.getAvailableLocales());

                        calendar = Calendar.getInstance();
                        calendar.clear();
                        calendar.set(Calendar.HOUR, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);

                        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                        startTime = formatter.format(new Time(calendar.getTimeInMillis()));

                        reminderTimesTextView.setText(startTime);
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
        reminderDateTextView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                mPickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                        startDate = Year + "-" + (Month + 1) + "-" + Day;

                        calendar = Calendar.getInstance();
                        calendar.clear();
                        calendar.set(Calendar.MONTH, Month);
                        calendar.set(Calendar.DAY_OF_MONTH, Day);
                        calendar.set(Calendar.YEAR, Year);

                        SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.dateformate));
                        startDate = dateFormatter.format(new Date(calendar.getTimeInMillis()));

                        reminderDateTextView.setText(startDate);

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

         /*
        Save Button
         */
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if (calendar.getTimeInMillis() > calendar.getTimeInMillis()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    alertDialog.setTitle("Warning !!");
                    alertDialog.setMessage("Invalid time");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                }

                completeText = billAutoCompleteTextView.getText().toString().trim();
                startTime = reminderTimesTextView.getText().toString().trim();
                startDate = reminderDateTextView.getText().toString().trim();

                mBillHelper.insertBillName(completeText);

                if (completeText.isEmpty() || startDate.isEmpty() || startTime.isEmpty()) {
                    if (completeText.isEmpty()) {
                        billAutoCompleteTextView.setError(getString(R.string.billName_is_required));
                        billAutoCompleteTextView.requestFocus();
                    }

                    if (startDate.isEmpty()) {
                        reminderDateTextView.setError(getString(R.string.date_is_required));
                        reminderDateTextView.requestFocus();
                    }

                    if (startTime.isEmpty()) {
                        reminderTimesTextView.setError(getString(R.string.time_is_required));
                        reminderTimesTextView.requestFocus();
                    }


                    return;
                }

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
                            startTime = formatter.format(new Date(calendar.getTimeInMillis()));
                            SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.dateformate));
                            startDate = dateFormatter.format(new Date(calendar.getTimeInMillis()));

                            AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                            Intent intent = new Intent(rootView.getContext(), AlarmReceiver.class);

                            intent.putExtra(getString(R.string.alert_title), completeText);
                            intent.putExtra(getString(R.string.bill_name), completeText);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(rootView.getContext()
                                    , 0, intent, 0);

                            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            mBillHelper.insertBillTime_Date(startTime, startDate);
                        }

                        else if (checkedId == R.id.notify) {

                            reminder = 2;
                            Intent intent = new Intent(rootView.getContext(), NotificationManager2.class);

                            intent.putExtra(getString(R.string.alert_title), "Reminder");
                            intent.putExtra(getString(R.string.bill_name), completeText);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext()
                                    , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                            calendar = Calendar.getInstance();
                            calendar.clear();
                            calendar.set(Calendar.MONTH, datePickMonth);
                            calendar.set(Calendar.DAY_OF_MONTH, datePickDay);
                            calendar.set(Calendar.YEAR, datePickYear);
                            calendar.set(Calendar.HOUR, timePickHour);
                            calendar.set(Calendar.MINUTE, timePickMinute);
                            calendar.set(Calendar.SECOND, 00);


                            SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                            startTime = formatter.format(new Date(calendar.getTimeInMillis()));
                            SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.dateformate));
                            startDate = dateFormatter.format(new Date(calendar.getTimeInMillis()));


                            billAutoCompleteTextView = rootView.findViewById(R.id.bill_autocomplete);
                            completeText = billAutoCompleteTextView.getText().toString().trim();

                            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            mBillHelper.insertBillTime_Date(startTime, startDate);

                        }


                        else if (checkedId == R.id.both){

                            reminder = 3;
                            Intent intent2 = new Intent(rootView.getContext(), NotificationManager2.class);

                            intent2.putExtra(getString(R.string.alert_title), "Reminder");
                            intent2.putExtra(getString(R.string.bill_name), completeText);

                            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getContext()
                                    , 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                            calendar = Calendar.getInstance();
                            calendar.clear();
                            calendar.set(Calendar.MONTH, datePickMonth);
                            calendar.set(Calendar.DAY_OF_MONTH, datePickDay);
                            calendar.set(Calendar.YEAR, datePickYear);
                            calendar.set(Calendar.HOUR, timePickHour);
                            calendar.set(Calendar.MINUTE, timePickMinute);
                            calendar.set(Calendar.SECOND, 00);


                            SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                            startTime = formatter.format(new Date(calendar.getTimeInMillis()));
                            SimpleDateFormat dateFormatter = new SimpleDateFormat(getString(R.string.dateformate));
                            startDate = dateFormatter.format(new Date(calendar.getTimeInMillis()));

                            Intent intent = new Intent(rootView.getContext(), AlarmReceiver.class);

                            completeText = billAutoCompleteTextView.getText().toString().trim();

                            intent.putExtra(getString(R.string.alert_title), completeText);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(rootView.getContext()
                                    , 0, intent, 0);

                            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            mBillHelper.insertBillTime_Date(startTime, startDate);


                            billAutoCompleteTextView = rootView.findViewById(R.id.bill_autocomplete);
                            String alertTitle = billAutoCompleteTextView.getText().toString().trim();
                            intent.putExtra(getString(R.string.alert_title), alertTitle);

                            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent2);

                            mBillHelper = new BillDbHelper(rootView.getContext());
                            mBillHelper.insertBillTime_Date(startTime, startDate);
                        }
                    }
                });

                mBillHelper = new BillDbHelper(rootView.getContext());
                long newRowId = mBillHelper.insertBill2(completeText, startTime, startDate, reminder);
                billAutoCompleteTextView.setText("");
                reminderTimesTextView.setText("");
                reminderDateTextView.setText("");
                radioGroup.clearCheck();
                if (newRowId == -1) {
                    Toast.makeText(getContext(), getResources().getString(R.string.reAdd_bill),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.bill_added_success),
                            Toast.LENGTH_SHORT).show();
                }
                Intent openMainScreen = new Intent(rootView.getContext(), BillsActivity.class);
                openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openMainScreen);
            }
        });
        return rootView;
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