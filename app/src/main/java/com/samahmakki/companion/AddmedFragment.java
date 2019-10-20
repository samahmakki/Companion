package com.samahmakki.companion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class AddmedFragment extends Fragment {

    private final static int REQUEST_IMAGE_CAPTURE = 1;
    private final static int REQUEST_CAMERA = 3;
    private final static int PERMISSION_CODE = 2;
    public static DBHelper dbHelper;
    ImageButton btnopen;
    EditText etadd;
    TextView starttime;
    String startTime;
    ImageButton btnpick;
    Bitmap bitmap;
    Button addbtn;
    Uri imageUri;
    ImageView imageView;
    TextView etdate;
    TextView startdate;
    String interval;
    LinearLayout visiblelist;
    CheckBox checkBox;
    SQLiteDatabase db;
    int timePickHour;
    int timePickMinute;
    int datePickMonth;
    int datePickDay;
    int datePickYear;
    Calendar calendar;
    DatePickerDialog mPickerDialog;
    int year, month, day, hour, minute;
    String startDate;
    AlarmManager alarmMgr;
    PendingIntent pendingIntent;
    RadioGroup freqradio, durradio;


    RadioButton once, twice, thrice, four, forever, twoweek, tenday, week, radioButton;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_add, container, false);

        //VIEWS
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(getString(R.string.new_medicine));
        etdate = view.findViewById(R.id.et_Date);

        visiblelist = view.findViewById(R.id.visiblelist);
        etadd = view.findViewById(R.id.addet);
        btnopen = view.findViewById(R.id.openbtn);
        imageView = view.findViewById(R.id.img);
        checkBox = view.findViewById(R.id.chckbox);
        btnpick = view.findViewById(R.id.pickbtn);
        addbtn = view.findViewById(R.id.addmedbtn);
        startdate = view.findViewById(R.id.start_date);
        once = view.findViewById(R.id.once);
        twice = view.findViewById(R.id.twice);
        thrice = view.findViewById(R.id.thrice);
        four = view.findViewById(R.id.four);
        week = view.findViewById(R.id.weekly);
        forever = view.findViewById(R.id.forever);
        tenday = view.findViewById(R.id.tenday);
        twoweek = view.findViewById(R.id.twoweek);
        freqradio = view.findViewById(R.id.fradio_group);
        durradio = view.findViewById(R.id.dradio_group);


        starttime = view.findViewById(R.id.start_time);
        dbHelper = new DBHelper(getContext(), "RafeeqDB", null, 5);
        db = dbHelper.getWritableDatabase();
        //creating table in database
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS MEDICINE (id INTEGER PRIMARY KEY AUTOINCREMENT ,name VARCHAR,image BLOB,date VARCHAR,time VARCHAR,interval VARCHAR)");


        //capture picture by camera

        btnopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if system os is >= marshmallow,request runtime permission

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                            || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                        //permission is not enabled,request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        //show popup for runtime permission
                        requestPermissions(permission, PERMISSION_CODE);

                    } else {  //permission is already granted
                        openCamera();
                    }
                } else {

                    //system os is less than marshmallow
                    openCamera();
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    visiblelist.setVisibility(View.VISIBLE);
                    //alarmSet();

                } else {
                    visiblelist.setVisibility(View.GONE);
                }
            }
        });

        starttime.setOnClickListener(new View.OnClickListener() {
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
                        starttime.setText(startTime);
                        timePickHour = timePicker.getCurrentHour();
                        timePickMinute = timePicker.getCurrentMinute();
                    }
                }, hour, minute, false);
                mTimePicker.setTitle(getString(R.string.select_time));
                mTimePicker.show();
            }
        });


        startdate.setOnClickListener(new View.OnClickListener()

        {
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
                        startdate.setText(startDate);

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


        // pick image from gallery

        btnpick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check runtime permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted,request it
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else { //permission is already granted
                        pickImageFromGallery();


                    }
                }
            }
        });

        //add to sqlite
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                calendar.set(Calendar.HOUR, timePickHour);
                calendar.set(Calendar.MINUTE, timePickMinute);
                calendar.set(Calendar.SECOND, 0);
                String name = etadd.getText().toString().trim();
                startDate = startdate.getText().toString().trim();
                startTime = starttime.getText().toString().trim();
                if (name.isEmpty() || startDate.isEmpty() || startTime.isEmpty() || imageView.getDrawable() == null || (freqradio.getCheckedRadioButtonId() == -1) || (c.getTimeInMillis() > calendar.getTimeInMillis())) {
                    if (name.isEmpty()) {
                        etadd.setError(getString(R.string.medName_is_required));
                        etadd.requestFocus();
                    }

                    if (startDate.isEmpty()) {
                        startdate.setError(getString(R.string.date_is_required));
                        startdate.requestFocus();
                    }

                    if (startTime.isEmpty()) {
                        starttime.setError(getString(R.string.time_is_required));
                        starttime.requestFocus();
                    }
                    if (imageView.getDrawable() == null) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        alertDialog.setTitle("Warning !!");
                        alertDialog.setMessage("Please Insert Picture");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                    if (c.getTimeInMillis() > calendar.getTimeInMillis()) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        alertDialog.setTitle("Warning !!");
                        alertDialog.setMessage("Invalid time");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }

                    if (freqradio.getCheckedRadioButtonId() == -1) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Warning !!");
                        alertDialog.setMessage("Please select one Option");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }


                    return;
                }


                if (checkBox.isChecked()) {

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
                    Intent intent = new Intent(getContext(), AlarmReceiver.class);

                    String alertTitle = etadd.getText().toString();
                    intent.putExtra(getString(R.string.medicine_name), alertTitle);

                    pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    dbHelper.insertMEDTime_Date(startTime, startDate, interval);
                }

                final int radioId = freqradio.getCheckedRadioButtonId();
                radioButton = view.findViewById(radioId);
                interval = radioButton.getText().toString();


                freqradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (radioId == R.id.once) {
                           /* calendar.set(Calendar.HOUR, timePickHour);
                            calendar.set(Calendar.MINUTE, timePickMinute);
                            calendar.set(Calendar.SECOND, 0);*/
                            long startUpTime = calendar.getTimeInMillis();
                            if (System.currentTimeMillis() > startUpTime) {
                                startUpTime = startUpTime +24*60*60*1000 ;
                            }


                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_DAY, pendingIntent);
                        }
                        if (radioId == R.id.twice) {
                            /* calendar.set(Calendar.HOUR, timePickHour);
                            calendar.set(Calendar.MINUTE, timePickMinute);
                            calendar.set(Calendar.SECOND, 0);*/
                            long startUpTime = calendar.getTimeInMillis();
                            if (System.currentTimeMillis() > startUpTime) {
                                startUpTime = startUpTime +12*60*60*1000 ;
                            }

                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_HALF_DAY, pendingIntent);

                        }
                        if (radioId == R.id.thrice) {
                            /* calendar.set(Calendar.HOUR, timePickHour);
                            calendar.set(Calendar.MINUTE, timePickMinute);
                            calendar.set(Calendar.SECOND, 0);*/

                            long startUpTime = calendar.getTimeInMillis();
                            if (System.currentTimeMillis() > startUpTime) {
                                startUpTime = startUpTime +  8*60*60*1000 ;
                            }
                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_HOUR * 8, pendingIntent);
                        }
                        if (radioId == R.id.four) {

                          /*  alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    SystemClock.elapsedRealtime() +
                                            60 * 1000, pendingIntent);*/
                            /* calendar.set(Calendar.HOUR, timePickHour);
                            calendar.set(Calendar.MINUTE, timePickMinute);
                            calendar.set(Calendar.SECOND, 0);*/
                            long startUpTime = calendar.getTimeInMillis();
                            if (System.currentTimeMillis() > startUpTime) {
                                startUpTime = startUpTime + 6*60*60*1000 ;
                            }

                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_HOUR * 6, pendingIntent);
                        }


                    }
                });


                try {
                    dbHelper.insertData(etadd.getText().toString().trim(),
                            imageViewToByte(imageView),
                            startdate.getText().toString().trim(),
                            starttime.getText().toString().trim(),
                            radioButton.getText().toString().trim()


                    );
                    Toast.makeText(getContext(), getString(R.string.added_successfully), Toast.LENGTH_SHORT).show();

                    //Reset views
                    etadd.setText("");
                    imageView.setImageResource(android.R.color.transparent);
                    startdate.setText("");
                    starttime.setText("");
                    radioButton.setChecked(false);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*ContentValues cv = new ContentValues();
                cv.put(DBHelper.name, etadd.toString());
                cv = new ContentValues();
                cv.put(DBHelper.date, startdate.toString());
                cv.put(DBHelper.time, startTime);*/


           /*     Fragment addmedFragment = new AddmedFragment();
                FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                transaction.replace(R.id.fragment_container, addmedFragment);
                // if written, this transaction will be added to backstack
                transaction.addToBackStack(null);
                transaction.commit();*/
                Fragment kitFragment = new KitFragment();
                FragmentTransaction transaction = (getFragmentManager()).beginTransaction();
                transaction.replace(R.id.fragment_container, kitFragment);
                // if written, this transaction will be added to backstack
                transaction.addToBackStack(null);
                transaction.commit();

            }





        });

        return view;


    }


    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imageUri = Objects.requireNonNull(getActivity()).getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //camera intent
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(camIntent, REQUEST_CAMERA);

    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    /* public void createNotification() {
      Intent myIntent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext() , NotifyService. class ) ;
      AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
      PendingIntent pendingIntent = PendingIntent. getService ( getContext(), 0 , myIntent , 0 ) ;
      Calendar calendar = Calendar. getInstance () ;
      calendar.set(Calendar. SECOND , 0 ) ;
      calendar.set(Calendar. MINUTE , 0 ) ;
      calendar.set(Calendar. HOUR , 0 ) ;
      calendar.set(Calendar. AM_PM , Calendar. AM ) ;
      calendar.add(Calendar. DAY_OF_MONTH , 1 ) ;
      alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() +
                   60 * 1000, pendingIntent);
     alarmManager.setRepeating(AlarmManager. RTC_WAKEUP , calendar.getTimeInMillis() , 1000 * 60 * 60 * 24 , pendingIntent) ;
     }*/


    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE);
    }


    public String con(int time) {
        if (time >= 10) {
            return String.valueOf(time);
        } else {
            return "0" + String.valueOf(time);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                    openCamera();
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

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //set image to image view

            imageView.setImageURI(data.getData());

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            //set captured image to our imageView

            imageView.setImageURI(imageUri);


        }


    }


}











