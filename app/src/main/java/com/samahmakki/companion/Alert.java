package com.samahmakki.companion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

public class Alert extends Activity {

    MediaPlayer mp;
    int reso = R.raw.alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        mp = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);
        mp.start();
        String msg = getString(R.string.alarmtext) + "Rafeeq";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // String msg = getString(R.string.alarmtext) + Objects.requireNonNull(getIntent().getExtras()).getString( getString(R.string.title_msg));
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.ok)
                ,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //set intent to open activity
                      /*  Intent i = new Intent(Alert.this, BillsActivity.class);
                        startActivity(i);*/

                        dialog.dismiss();
                        Alert.this.finish();
                    }
                });

     /*builder.setNegativeButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

                        dialog.dismiss();
                        Alert.this.finish();

                    }

                });*/
        //AlertDialog alert = builder.create();
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
    }

}
