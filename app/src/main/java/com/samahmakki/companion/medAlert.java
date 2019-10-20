package com.samahmakki.companion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class medAlert extends Activity {



        MediaPlayer mp;
        int reso = R.raw.alarm;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.medalert);

            mp = MediaPlayer.create(getApplicationContext(), reso);
            mp.start();
            String msg = getString(R.string.alarmtext) + "It's Medication Time!!";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // String msg = getString(R.string.alarmtext) + Objects.requireNonNull(getIntent().getExtras()).getString( getString(R.string.title_msg));
            builder.setMessage(msg);
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.ok)
                    ,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //set intent to open activity
                           // Intent i = new Intent(medAlert.this, MedicationActivity.class);
                          //  startActivity(i);

                            dialog.dismiss();
                           medAlert.this.finish();
                        }
                    });


          //  AlertDialog alert = builder.create();
          builder.show();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mp.release();
        }

    }




