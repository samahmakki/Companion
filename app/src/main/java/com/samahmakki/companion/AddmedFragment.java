package com.samahmakki.companion;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;
import static com.samahmakki.companion.DBHelper.date;

public class AddmedFragment extends Fragment {

    private final static int REQUEST_IMAGE_CAPTURE = 1;
    private final static int REQUEST_CAMERA = 3;
    private final static int PERMISSION_CODE = 2;
    public static DBHelper dbHelper;
    ImageButton btnopen;
    EditText etadd;
    Calendar currentTime;
    TextView starttime;
    String startTime;
    int hour, minute;
    ImageButton btnpick;
    Bitmap bitmap;
    Button addbtn;
    Uri imageUri;
    ImageView imageView;
    TextView etdate;
    TextView startdate;
    LinearLayout visiblelist;
    CheckBox checkBox;
    DatePicker pickerDate;
    TimePicker pickerTime;
    SQLiteDatabase db;

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_add, container, false);

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

        starttime = view.findViewById(R.id.start_time);
        dbHelper = new DBHelper(getContext(), "RafeeqDB", null, 1);
        //creating table in database
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS MEDICINE (id INTEGER PRIMARY KEY AUTOINCREMENT ,name VARCHAR,image BLOB,date VARCHAR,time VARCHAR)");

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbHelper.insertData(etadd.getText().toString().trim(),
                            imageViewToByte(imageView),
                            startdate.getText().toString().trim(),
                            starttime.getText().toString().trim()


                    );
                    Toast.makeText(getContext(), getString(R.string.added_succefully), Toast.LENGTH_SHORT).show();

                    //Reset views
                    etadd.setText("");
                    imageView.setImageResource(R.drawable.add_new_photo);
                    startdate.setText("");
                    starttime.setText("");


                } catch (Exception e) {
                    e.printStackTrace();
                }

                ContentValues cv = new ContentValues();
                cv.put(DBHelper.name, etadd.toString());
                cv.put(date, startdate.toString());
                cv.put(DBHelper.time, startTime);
                if (checkBox.isChecked()) {

                    starttime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            currentTime = Calendar.getInstance();
                            hour = currentTime.get(Calendar.HOUR_OF_DAY);
                            minute = currentTime.get(Calendar.MINUTE);

                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    startTime = con(selectedHour) + ":" + con(selectedMinute);
                                    starttime.setText(startTime);
                                    // alarmStartTime = mCurrentTime.getTimeInMillis();
                                }
                            }, hour, minute, false);//Yes 24 hour time
                            mTimePicker.setTitle(getString(R.string.select_time));
                            mTimePicker.show();
                        }
                    });


                    startdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar calendar = Calendar.getInstance();
                            final int year = calendar.get(Calendar.YEAR);
                            final int month = calendar.get(Calendar.MONTH);
                            final int day = calendar.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    month = month + 1;
                                    String date = day + "/" + month + "/" + year;
                                    startdate.setText(date);


                                }
                            }, year, month, day);
                            datePickerDialog.show();
                        }
                    });
                    Calendar calender = Calendar.getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));

                    String timeString = formatter.format(new Date(calender.getTimeInMillis()));
                    SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
                    String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));

                    AlarmManager alarmMgr = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getContext(), AlarmReciever.class);
                    String alertTitle = etadd.getText().toString();
                    intent.putExtra(getString(R.string.medicine_name), alertTitle);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                    cv = new ContentValues();
                    cv.put(DBHelper.time, timeString);
                    cv.put(date, dateString);

                }

                db.insert(dbHelper.getDatabaseName(), null, cv);
                Intent openMainScreen = new Intent(getContext(), KitFragment.class);
                openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openMainScreen);

            }
        });

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


        return view;


    }

    //private void notificationset() {
    //   Calendar calender = Calendar.getInstance();
    // calender.clear();
    // calender.set(Calendar.MONTH, pickerDate.getMonth());
    //  calender.set(Calendar.DAY_OF_MONTH, pickerDate.getDayOfMonth());
    // calender.set(Calendar.YEAR, pickerDate.getYear());
    // calender.set(Calendar.HOUR, pickerTime.getCurrentHour());
    //  calender.set(Calendar.MINUTE, pickerTime.getCurrentMinute());
    //calender.set(Calendar.SECOND, 0);

    //SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
    // String timeString = formatter.format(new Date(calender.getTimeInMillis()));
    //SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
    //String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));

    // AlarmManager alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    //Intent intent = new Intent(getContext(), NotificationManager2.class);
    //tring alertTitle = etadd.getText().toString();
    //intent.putExtra(getString(R.string.medicine_name), alertTitle);
    // intent.putExtra(getString(R.string.alert_content), alertContent);

    //PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
    //ContentValues cv = new ContentValues();

    //alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
    //cv.put(dbHelper.time, timeString);
    //cv.put(dbHelper.date, dateString);
    //}


    // private void alarmSet() {

    // Calendar calender = Calendar.getInstance();
    //   calender.clear();
    //   calender.set(Calendar.MONTH, pickerDate.getMonth());
    //  calender.set(Calendar.DAY_OF_MONTH, pickerDate.getDayOfMonth());
    //  calender.set(Calendar.YEAR, pickerDate.getYear());
    //  calender.set(Calendar.HOUR, pickerTime.getCurrentHour());
    //  calender.set(Calendar.MINUTE, pickerTime.getCurrentMinute());
    //  calender.set(Calendar.SECOND, 0);

    //  SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
    // String timeString = formatter.format(new Date(calender.getTimeInMillis()));
    //  SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
    //  String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));

    // AlarmManager alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
    // Intent intent = new Intent(getContext(), AlarmReciever.class);

    // String alertTitle = etadd.getText().toString();
    //intent.putExtra(getString(R.string.medicine_name), alertTitle);

    //  PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
    //  ContentValues cv = new ContentValues();

    // alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
    //   cv.put(DBHelper.time, timeString);
    //  cv.put(DBHelper.date, dateString);


    //}


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

    // public void createNotification() {
    //   Intent myIntent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext() , NotifyService. class ) ;
    //  AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
    //  PendingIntent pendingIntent = PendingIntent. getService ( getContext(), 0 , myIntent , 0 ) ;
    // Calendar calendar = Calendar. getInstance () ;
    // calendar.set(Calendar. SECOND , 0 ) ;
    // calendar.set(Calendar. MINUTE , 0 ) ;
    //  calendar.set(Calendar. HOUR , 0 ) ;
    //calendar.set(Calendar. AM_PM , Calendar. AM ) ;
    //  calendar.add(Calendar. DAY_OF_MONTH , 1 ) ;
    //  alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
    //         SystemClock.elapsedRealtime() +
    //                 60 * 1000, pendingIntent);
    // alarmManager.setRepeating(AlarmManager. RTC_WAKEUP , calendar.getTimeInMillis() , 1000 * 60 * 60 * 24 , pendingIntent) ;
    // }


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











