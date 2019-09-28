package com.samahmakki.companion;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;

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
    int hour,minute;
    ImageButton btnpick;
    Bitmap bitmap;
    Button addbtn;
    Uri imageUri;
    ImageView imageView;

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
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity)Objects.requireNonNull(getActivity())).getSupportActionBar();
        actionBar.setTitle(getString(R.string.new_medicine));

        etadd = view.findViewById(R.id.addet);
        btnopen = view.findViewById(R.id.openbtn);
        imageView = view.findViewById(R.id.img);
        btnpick = view.findViewById(R.id.pickbtn);
        addbtn = view.findViewById(R.id.addmedbtn);
        starttime =view.findViewById(R.id.start_time);
        dbHelper = new DBHelper(getContext(),"RafeeqDB",null,1);
         //creating table in database
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS MEDICINE (id INTEGER PRIMARY KEY AUTOINCREMENT ,name VARCHAR,image BLOB)");



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
            try{  dbHelper.insertData(etadd.getText().toString().trim(),
                      imageViewToByte(imageView)

              );
              Toast.makeText(getContext(),getString(R.string.added_succefully),Toast.LENGTH_SHORT).show();

              //Reset views
               etadd.setText("");
               imageView.setImageResource(R.drawable.add_new_photo);
          }
          catch (Exception e){
              e.printStackTrace();
          }}
      });


        return view;


    }
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[]byteArray = stream.toByteArray();
        return byteArray;
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













