package com.samahmakki.companion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;
import static com.samahmakki.companion.AddmedFragment.dbHelper;

public class KitFragment extends Fragment {

    ListView lstmed;
    MedicineListAdapter adapter = null;
    ArrayList<medicine>list;
    ImageView imgView;
    int year, month, day, hour, minute;
    String startDate;
    int datePickMonth;
    int datePickDay;
    int datePickYear;
    String startTime;
    Calendar calendar = Calendar.getInstance();
    int timePickHour;
    int timePickMinute;
    DatePickerDialog mPickerDialog;
    AlarmManager alarmMgr;
    PendingIntent pendingIntent;
    String repeat;

    RadioButton once, twice, thrice, four, forever, twoweek, tenday, week, radioButton;
    private SQLiteDatabase db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_kit, container, false);

        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Medication list");

        lstmed = view.findViewById(R.id.lst_med);
        list = new ArrayList<>();

        adapter = new MedicineListAdapter(getContext(), R.layout.item_medicine, list);
        //get all data from SQLite
        db = dbHelper.getWritableDatabase();
        Cursor cursor;
        cursor = dbHelper.getData("SELECT * FROM MEDICINE");
        lstmed.setAdapter(adapter);

        list.clear();


        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);
            String interval = cursor.getString(5);

            list.add(new medicine(id, name, image,date,time,interval));
        }
        adapter.notifyDataSetChanged();

        if (list.size() == 0) {
            //if there is no medicine in table of database which mean listview is empty
            Toast.makeText(getContext(), "No Medicine Found", Toast.LENGTH_SHORT).show();
        }

        lstmed.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                CharSequence[] items = { "Update","Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder((Objects.requireNonNull(getContext())));
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            //update
                            Cursor c = dbHelper.getData("SELECT id FROM MEDICINE");
                            ArrayList<Integer> arrID = new ArrayList<>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            //show update dialog

                            showDialogUpdate(getActivity(), arrID.get(position));
                        }


                        if (which == 1){

                            //delete
                            Cursor c = dbHelper.getData("SELECT id FROM MEDICINE");
                            ArrayList<Integer> arrID = new ArrayList<>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }





                    }
                });
                dialog.show();
                return true;
            }
        });
        return view;
    }

    private void showDialogDelete(final int medicineID) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        dialogDelete.setTitle("Warning !!");
        dialogDelete.setMessage("Are you sure you want delete this?");
        dialogDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dbHelper.deleteData(medicineID);
                    Toast.makeText(getContext(), "Deleted Succefully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("error",e.getMessage());
                }
                updateMedicineList();

            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(final Activity activity, final int position) {

        final Dialog dialog = new Dialog(activity);

        dialog.setContentView(R.layout.update_medicinelist);
        dialog.setTitle("Update");
        imgView = dialog.findViewById(R.id.img);
        final EditText edtName = dialog.findViewById(R.id.edtname);
        final TextView edtdate  = dialog.findViewById(R.id.edtdate);
        final TextView edttime = dialog.findViewById(R.id.edttime);
        final RadioGroup freqradio = dialog.findViewById(R.id.fradio_group);


        Button btnUpdate = dialog.findViewById(R.id.upbtn);
        //set width of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height of dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        dialog.show();

        //in update dialog open camera to update image
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted,request it
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, 888);
                    } else { //permission is already granted
                        pickImageFromGallery();


                    }
                }


            }
        });

        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                mPickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                        startDate = Year + "-" + (Month + 1) + "-" + Day;
                        edtdate.setText(startDate);

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


        edttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime = con(selectedHour) + ":" + con(selectedMinute);
                        // String.format(startTime, Locale.getAvailableLocales());
                        edttime.setText(startTime);
                        timePickHour = timePicker.getCurrentHour();
                        timePickMinute = timePicker.getCurrentMinute();
                    }
                }, hour, minute, false);
                mTimePicker.setTitle(getString(R.string.select_time));
                mTimePicker.show();
            }
        });



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.MONTH, datePickMonth);
                calendar.set(Calendar.DAY_OF_MONTH, datePickDay);
                calendar.set(Calendar.YEAR, datePickYear);
                calendar.set(Calendar.HOUR, timePickHour);
                calendar.set(Calendar.MINUTE, timePickMinute);
                calendar.set(Calendar.SECOND, 0);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                startTime = formatter.format(new Date(calendar.getTimeInMillis()));
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
                startDate = dateformatter.format(new Date(calendar.getTimeInMillis()));

                alarmMgr = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(), medAlarmReceiver.class);

                String alertTitle = edtName.getText().toString();
                intent.putExtra(getString(R.string.medicine_name), alertTitle);

                pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                // dbHelper.insertMEDTime_Date(startTime, startDate, repeat);


                if (freqradio != null){
                    freqradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            final int radioId = freqradio.getCheckedRadioButtonId();

                            radioButton =freqradio.findViewById(radioId);

                            repeat= radioButton.getText().toString();

                            if (radioId == R.id.once) {

                                long startUpTime = calendar.getTimeInMillis();
                                if (System.currentTimeMillis() > startUpTime) {
                                    startUpTime = startUpTime + 24*60*60*1000 ;
                                }


                                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_DAY, pendingIntent);
                            }
                            if (radioId == R.id.twice) {

                                long startUpTime = calendar.getTimeInMillis();
                                if (System.currentTimeMillis() > startUpTime) {
                                    startUpTime = startUpTime +12*60*60*1000 ;
                                }

                                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_HALF_DAY, pendingIntent);

                            }
                            if (radioId == R.id.thrice) {


                                long startUpTime = calendar.getTimeInMillis();
                                if (System.currentTimeMillis() > startUpTime) {
                                    startUpTime = startUpTime +8*60*60*1000 ;
                                }
                                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_HOUR * 8, pendingIntent);
                            }
                            if (radioId == R.id.four) {

                          /*  alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    SystemClock.elapsedRealtime() +
                                            60 * 1000, pendingIntent);*/

                                long startUpTime = calendar.getTimeInMillis();
                                if (System.currentTimeMillis() > startUpTime) {
                                    startUpTime = startUpTime + 6*60*60*1000;
                                }

                                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_HOUR * 6, pendingIntent);
                            }


                        }
                    });}




                try {
                    dbHelper.updateData(
                            edtName.getText().toString().trim(),
                            AddmedFragment.imageViewToByte(imgView),
                            edtdate.getText().toString().trim(),
                            edttime.getText().toString().trim(),
                            radioButton.getText().toString().trim()
                            ,                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Updated Succefully", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateMedicineList();
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

    private void updateMedicineList() {
        //get all data from sqlite
        Cursor cursor = dbHelper.getData("SELECT * FROM MEDICINE WHERE id = ?");
        list.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                byte[] image = cursor.getBlob(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                String interval = cursor.getString(5);

                list.add(new medicine(id, name, image,date,time,interval));
            }
        }
        adapter.notifyDataSetChanged();

    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 888);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 888: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();

                } else {
                    //permission was denied
                    Toast.makeText(getContext(), "Permission denied..!", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 888 && resultCode == RESULT_OK) {
            //set image to image view
            imgView.setImageURI(data.getData());


        }
    }




}
