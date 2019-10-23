package com.samahmakki.companion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class medAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String Title = intent.getStringExtra(context.getString(R.string.medicine_name));
        Intent x = new Intent(context, medAlert.class);
        x.putExtra(context.getString(R.string.medications), Title);
        x.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(x);

    }
}
