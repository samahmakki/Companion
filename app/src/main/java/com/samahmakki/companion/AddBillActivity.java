package com.samahmakki.companion;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
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

import java.util.Calendar;
import com.kd.dynamic.calendar.generator.ImageGenerator;

public class AddBillActivity extends AppCompatActivity {

    String[] bills = {"فاتورة كهرباء", "فاتورة مياه", "فاتورة غاز", "فاتورة انترنت", "فاتورة تليفون"};

    AutoCompleteTextView billAutoCompleteTextView;
    String completeText;

    TextView reminderTimesTextView, reminderDateTextView;
    String reminderTimes;

    Calendar mCurrentTime;
    Calendar mCurrentDate;

    DatePickerDialog mPickerDialog;

    int year, month, day, hour, minute;
    String startDate;
    String startTime;

    RadioGroup radioGroup;
    RadioButton monthlyRadioButton, weeklyRadioButton;

    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        reminderTimesTextView = findViewById(R.id.reminder_times);
        reminderDateTextView = findViewById(R.id.reminder_date);

        radioGroup = findViewById(R.id.radio_group);
        monthlyRadioButton = findViewById(R.id.monthly);
        weeklyRadioButton = findViewById(R.id.weekly);

        saveButton = findViewById(R.id.save_button);

        /*
        AutoCompleteText for bills
         */
        //Creating the instance of ArrayAdapter containing list of bills names
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, bills);
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
                mTimePicker = new TimePickerDialog(AddBillActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                //final String abc= getAge(year, month, day);

                mPickerDialog = new DatePickerDialog(AddBillActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                        startDate = Year + "-" + (Month + 1) + "-" + Day;
                        reminderDateTextView.setText(startDate);
                        //  Toast.makeText(RegisterPatientActivity.this,"Your age= "+abc, Toast.LENGTH_LONG).show();

                        mCurrentDate.set(Year, (Month + 1), Day);
                        //   mImageGenerator.generateDateImage(mCurrentDate, R.drawable.empty_calendar);
                    }
                }, year, month, day);
                mPickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mPickerDialog.setTitle("Select Date");
                mPickerDialog.show();
            }
        });
     //to start calender
        ImageGenerator mImageGenerator = new ImageGenerator(AddBillActivity.this);

     // Set the icon size to the generated in dip.
        mImageGenerator.setIconSize(50, 50);

     // Set the size of the date and month font in dip.
        mImageGenerator.setDateSize(30);
        mImageGenerator.setMonthSize(10);

     // Set the position of the date and month in dip.
        mImageGenerator.setDatePosition(42);
        mImageGenerator.setMonthPosition(14);

     // Set the color of the font to be generated
        mImageGenerator.setDateColor(Color.parseColor("#3c6eaf"));
        mImageGenerator.setMonthColor(Color.WHITE);


       /*
        Radio Group
         */
       radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int i) {
               if(i == R.id.monthly) {
               } else if (i == R.id.weekly){

               }
           }
       });


     /*
        Save Button
         */
     saveButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
           // convert time from string to date variable
             // to force user to enter task data
             completeText = billAutoCompleteTextView.getText().toString();
             startDate = reminderDateTextView.getText().toString();
             startTime = reminderTimesTextView.getText().toString();

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
