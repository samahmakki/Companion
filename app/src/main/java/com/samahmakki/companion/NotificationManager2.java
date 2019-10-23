package com.samahmakki.companion;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


@SuppressWarnings("deprecation")
public class NotificationManager2 extends BroadcastReceiver {
    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {


        int requestCode=(int)System.currentTimeMillis();
        String Title = intent.getStringExtra(context.getString(R.string.alert_title));
        String content = intent.getStringExtra(context.getString(R.string.bill_name));



        Intent mainIntent = new Intent(context,BillsActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context,requestCode,mainIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Notification.Builder builder =
                new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_action_alarms)
                .setContentIntent(contentIntent)
                .setContentTitle(Title)
                .setWhen(System.currentTimeMillis())
                //.setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentText(content)
                .setAutoCancel(true)
                //.addAction(R.drawable.alert,"Snooze",)
                .setStyle(new Notification.BigTextStyle().bigText((content)))
                .setColor(Color.BLUE)

                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.getPackageName() + "/raw/notify"));

        // Add as notification


        manager.notify(0, builder.build());
    }
}