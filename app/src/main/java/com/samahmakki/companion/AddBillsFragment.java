package com.samahmakki.companion;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.samahmakki.companion.data.BillContract.BillEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddBillsFragment extends Fragment {

    AutoCompleteTextView billAutoCompleteTextView;
    String completeText;
    TextView reminderTimesTextView, reminderDateTextView;
    Calendar mCurrentTime;
    Calendar mCurrentDate;

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

        String[] bills = {getResources().getString(R.string.electricity_bill),
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
        reminderTimesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentTime = Calendar.getInstance();
                hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mCurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime = con(selectedHour) + ":" + con(selectedMinute);
                        String.format(startTime, Locale.getAvailableLocales());
                        reminderTimesTextView.setText(startTime);
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

                mPickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                        startDate = Year + "-" + (Month + 1) + "-" + Day;
                        reminderDateTextView.setText(startDate);

                        mCurrentDate.set(Year, (Month + 1), Day);
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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ContentValues cv = new ContentValues();

                completeText = billAutoCompleteTextView.getText().toString().trim();
                startDate = reminderDateTextView.getText().toString().trim();
                startTime = reminderTimesTextView.getText().toString().trim();

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

                        if (checkedId == R.id.alarm){

                            AlarmManager alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

                            Intent intent = new Intent(rootView.getContext(), AlarmReceiver.class);

                            completeText = billAutoCompleteTextView.getText().toString().trim();
                            intent.putExtra(getString(R.string.alert_title), completeText);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(rootView.getContext()
                                    , 0, intent, 0);

                            alarmMgr.set(AlarmManager.RTC_WAKEUP, mCurrentTime.getTimeInMillis(), pendingIntent);

                            /*cv.put(BillEntry.COLUMN_Bill_TIME, startTime);
                            cv.put(BillEntry.COLUMN_Bill_DATE, startDate);*/
                        }
                        else if (checkedId == R.id.notify){
                            AlarmManager alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(rootView.getContext(), NotificationManager2.class);
                            intent.putExtra(getString(R.string.alert_title), completeText);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(rootView.getContext()
                                    , 0, intent, 0);
                            alarmMgr.set(AlarmManager.RTC_WAKEUP, mCurrentTime.getTimeInMillis(), pendingIntent);

                            /*cv.put(BillEntry.COLUMN_Bill_TIME, startTime);
                            cv.put(BillEntry.COLUMN_Bill_DATE, startDate);*/
                        }
                        // db.insert(BillEntry.TABLE_NAME, null, cv);
                        //mBillHelper.insert(startTime, startDate);
                        Intent openMainScreen = new Intent(rootView.getContext(), BillsActivity.class);
                        openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(openMainScreen);
                    }
                });


                mBillHelper = new BillDbHelper(rootView.getContext());
                long newRowId = mBillHelper.insertBill(completeText, startTime, startDate);

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
}