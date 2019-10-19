package com.samahmakki.companion;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
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

        mp = MediaPlayer.create(getApplicationContext(), reso);
        mp.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       // String msg = getString(R.string.alarmtext) + Objects.requireNonNull(getIntent().getExtras()).getString( getString(R.string.title_msg));
        String msg = getString(R.string.alarmtext) + getIntent().getExtras().getString(getString(R.string.title_msg_bill));
        builder.setMessage(msg).setCancelable(
                false).setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Alert.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
    }

}
