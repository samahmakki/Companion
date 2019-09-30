package com.samahmakki.companion;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dominic on 13/04/2015.
 */
public class BillAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String Title = intent.getStringExtra(context.getString(R.string.titttle));
        Intent x = new Intent(context, BillAlert.class);
        x.putExtra(context.getString(R.string.titttle), Title);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(x);
    }
}

