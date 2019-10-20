package com.samahmakki.companion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class flashlightActivity extends AppCompatActivity  {
    ImageButton imgbtn;
    TextView tvopen;
    boolean isSwitchedOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);

        Intent i = new Intent(flashlightActivity.this, ShakeDetectionService.class);

        imgbtn = findViewById(R.id.imageFlash);
        tvopen = findViewById(R.id.tvOpen);

        startService(i);

    }

    public void toggle(View view) throws CameraAccessException {
        if (tvopen.getText().equals(getString(R.string.switchOnCamera))) {
            tvopen.setText(R.string.switchoffflash);
            toggleLED("on");
        } else {
            tvopen.setText(getString(R.string.switchOnCamera));
            toggleLED("off");

        }
    }

    public void toggleLED(String command) throws CameraAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String cameraId = null;
            CameraManager cameraManager = (CameraManager) getSystemService
                    (Context.CAMERA_SERVICE);

            if (cameraManager != null) {
                cameraId = cameraManager.getCameraIdList()[0];
            }

            if (cameraManager != null) {

                if (command.equals("on")) {
                    cameraManager.setTorchMode(cameraId, true);
                    isSwitchedOn = true;
                    imgbtn.setImageResource(R.drawable.switch_on);

                } else {
                    cameraManager.setTorchMode(cameraId, false);
                    isSwitchedOn = false;
                    imgbtn.setImageResource(R.drawable.switch_off);

                }
            } else {
                Toast.makeText(this, "Not Available for your phone", Toast.LENGTH_SHORT).show();
            }
        }
    }

  /*  @Override
    public void onBackPressed() {            super.onBackPressed();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String cameraId = null;


            if (cameraManager != null) {
                try {
                    cameraId = cameraManager.getCameraIdList()[0];
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

                try {
                    assert cameraId != null;
                    cameraManager.setTorchMode(cameraId, false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

                isSwitchedOn = false;
            imgbtn.setImageResource(R.drawable.switch_off);
        }}
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStop() {
        super.onStop();
        if(isSwitchedOn){
            try {
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isSwitchedOn){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    cameraManager.setTorchMode(cameraId, false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if(isSwitchedOn){
            try {
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
*/

}




