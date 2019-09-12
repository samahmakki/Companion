package com.samahmakki.companion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class flashlightActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 50;
    ImageButton imgbtn;
    Button btnEnable;
    private boolean flashLightStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);

        imgbtn = findViewById(R.id.imageFlash);
        btnEnable = findViewById(R.id.buttonEnable);
//check if the camera has flash light or not
        final boolean hasCameraFlash = getPackageManager().hasSystemFeature
                (PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnabled = ContextCompat.checkSelfPermission
                (this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED;

        btnEnable.setEnabled(!isEnabled);
        imgbtn.setEnabled(isEnabled);

//what happen when user click the button
        btnEnable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ActivityCompat.requestPermissions(flashlightActivity.this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
            }
        });

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraFlash) {
                    if (flashLightStatus)
                        flashLightOff();
                    else
                        flashLightOn();
                } else {
                    Toast.makeText(flashlightActivity.this, "No flash available on your device",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //when user open flashlight
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService
                (Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
            imgbtn.setImageResource(R.drawable.switch_on);
        } catch (CameraAccessException e) {
        }
    }

    //when user close the flash
    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService
                (Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
            imgbtn.setImageResource(R.drawable.switch_off);
        } catch (CameraAccessException e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    btnEnable.setEnabled(false);
                    btnEnable.setText("Camera Enabled");
                    imgbtn.setEnabled(true);
                } else {
                    Toast.makeText(flashlightActivity.this, "Permission Denied for The camera", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}



