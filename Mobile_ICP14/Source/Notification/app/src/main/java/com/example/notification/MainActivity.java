// Created by Vijaya Yeruva on 11/20/2020
// Reference: https://developer.android.com/guide/topics/ui/notifiers/notifications

package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // notificationId is a unique int for each notification that you must define
    public int notificationId = 1;
    public String CHANNEL_ID = "one";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the NotificationChannel
        // you should execute this code as soon as your app starts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);
            calendar.set(year, month, day);
            calendarView.setDate(calendar.getTimeInMillis());
            TextView textView = findViewById(R.id.textView);
            textView.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        });

        findViewById(R.id.createButton).setOnClickListener(e -> {
            Intent intent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, "Event from the Notification App!");
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "UMKC");

            long date = calendarView.getDate();
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date);
            intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

            startActivity(intent);
        });
    }

    public void sendNotification(View view) {
        // Set the notification's tap action
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, AlertDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create a basic notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "one");
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setContentTitle("Message");
        builder.setContentText("This is an example for notification app");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("This is an example for notification app"));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }
}