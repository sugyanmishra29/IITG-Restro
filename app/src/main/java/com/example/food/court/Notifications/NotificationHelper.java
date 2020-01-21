package com.example.food.court.Notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.food.court.App;
import com.example.food.court.MainActivity;
import com.example.food.court.R;

public class NotificationHelper {
    private static final String TAG = "NotificationHelper";
    public static void displayNotification(Context context, String title, String body) {

        Log.i(TAG, "displayNotification: inside notification helper");
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_email)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(1, mBuilder.build());

    }
}
