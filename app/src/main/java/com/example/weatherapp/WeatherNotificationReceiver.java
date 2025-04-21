package com.example.weatherapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.weatherapp.model.WeatherResponse;

public class WeatherNotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "weather_channel_id";
    WeatherResponse weatherResponse = new WeatherResponse();

    @Override
    public void onReceive(Context context, Intent intent) {

        String fakeWeather =  "Hello";
//weatherResponse.getName() + "," + weatherResponse.getMain().getTemp();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.rainyday)
                .setContentText("Weather Updates")
                .setContentText(fakeWeather)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        notificationManager.notify(101, builder.build());

    }

    private void createNotificationChannel(NotificationManager manager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            CharSequence name = "Weather Channel" + weatherResponse.getName();
            String description = "Channel for weather updates" + weatherResponse.getMain().getTemp();
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            manager.createNotificationChannel(channel);

        }
    }
}
