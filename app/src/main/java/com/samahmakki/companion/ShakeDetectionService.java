package com.samahmakki.companion;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.os.IBinder;
import android.os.Vibrator;

public class ShakeDetectionService extends Service implements SensorEventListener {

public  static final int Min_Time_Between_Shake = 3000;
SensorManager sensorManager = null;
Vibrator vibrator = null;
private long lastShakeTime = 0;
private boolean isFlashLightOn = false;
public static final Float shakeThreshold = 10.25f;
Utility utility;



public  ShakeDetectionService(){}

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        utility = new Utility(this);




        if(sensorManager != null){
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);


        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
        long curTime = System.currentTimeMillis();
        if((curTime - lastShakeTime)> Min_Time_Between_Shake) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double acceleration = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2))-SensorManager.GRAVITY_EARTH;
             if (acceleration > shakeThreshold){
                 lastShakeTime = curTime;
                 if (!isFlashLightOn){
                     try {
                         isFlashLightOn= utility.torchToggle("on");
                     } catch ( @SuppressLint("NewApi") CameraAccessException e) {
                         e.printStackTrace();
                     }

                 }
                 else {
                     try {
                         isFlashLightOn = utility.torchToggle("off");
                     } catch (@SuppressLint("NewApi") CameraAccessException e) {
                         e.printStackTrace();
                     }
                 }
             }

        }
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
