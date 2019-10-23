package com.samahmakki.companion;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dominic on 13/04/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {


            @Override
            public void onReceive(Context context, Intent intent) {
                String content = intent.getStringExtra(context.getString(R.string.bill_name));

                //Toast.makeText(context, context.getString(R.string.Alertnotifty) + intent.getStringExtra("title") , Toast.LENGTH_LONG).show();
                String Title = intent.getStringExtra(context.getString(R.string.alert_title));
                Intent x = new Intent(context, Alert.class);
                x.putExtra(context.getString(R.string.titttle), Title);
                x.putExtra(context.getString(R.string.title_msg_bill),content);
                x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(x);
            }

        }

