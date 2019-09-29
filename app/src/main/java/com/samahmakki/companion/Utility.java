package com.samahmakki.companion;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

class Utility {

    private boolean isSwitchedOn = false;
    Context context;

    Utility(Context context) {
        this.context = context;


    }


    boolean torchToggle(String command) throws CameraAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager cameraManager = (CameraManager)context.getSystemService
                    (Context.CAMERA_SERVICE);
            String cameraId = null;


            if (cameraManager != null) {
                cameraId = cameraManager.getCameraIdList()[0];
            }

            if (cameraManager != null) {

                if (command.equals("on")) {
                    cameraManager.setTorchMode(cameraId, true);
                    isSwitchedOn = true;
                } else {
                    cameraManager.setTorchMode(cameraId, false);
                    isSwitchedOn = false;
                }




            }

        }
        return isSwitchedOn;
    }

}



