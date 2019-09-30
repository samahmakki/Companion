package com.samahmakki.companion;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
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

import java.util.Calendar;

public class AddBillsFragment extends Fragment {

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
    String mReminderDays;
    int sReminderDays;
    RadioGroup radioGroup;
    RadioButton monthlyRadioButton, weeklyRadioButton;
    private Button saveButton;
    BillDbHelper mBillHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.add_bills_fragment, container, false);

        reminderTimesTextView = rootView.findViewById(R.id.reminder_times);
        reminderDateTextView = rootView.findViewById(R.id.reminder_date);

        radioGroup = rootView.findViewById(R.id.radio_group);
        monthlyRadioButton = rootView.findViewById(R.id.monthly);
        weeklyRadioButton = rootView.findViewById(R.id.weekly);

        saveButton = rootView.findViewById(R.id.save_button);

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

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime = con(selectedHour) + ":" + con(selectedMinute);
                        reminderTimesTextView.setText(startTime);
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

                mBillHelper = new BillDbHelper(rootView.getContext());
                long newRowId = mBillHelper.insertBill(completeText, startTime, startDate);
                billAutoCompleteTextView.setText("");
                reminderTimesTextView.setText("");
                reminderDateTextView.setText("");

                if (newRowId == -1) {
                    Toast.makeText(getContext(), "إعادة إضافة الفاتورة", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "تم إضافة الفاتورة", Toast.LENGTH_SHORT).show();
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