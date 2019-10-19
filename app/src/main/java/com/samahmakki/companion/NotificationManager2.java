package com.samahmakki.companion;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


@SuppressWarnings("deprecation")
public class NotificationManager2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String Title = intent.getStringExtra(context.getString(R.string.titttle));
        String content = intent.getStringExtra(context.getString(R.string.alert_content));




        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);
builder.setSmallIcon(R.drawable.ic_action_alarms)
        .setAutoCancel(true)
                        .setContentTitle(Title)
        .setWhen(System.currentTimeMillis())
        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setContentText(content)
                        .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.getPackageName() + "/raw/notify"));

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}