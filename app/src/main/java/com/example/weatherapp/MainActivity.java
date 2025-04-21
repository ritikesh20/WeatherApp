package com.example.weatherapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.adapter.WeatherAdapter;
import com.example.weatherapp.model.WeatherResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText editTextCityName;
    Button btnSearch;
    TextView tvCityName, tvTempWeather, tvHumidityWeather, tvMaxTempWeather, tvMinTempWeather, tvWindWeather, tvWeatherResult;
    ImageView imageWeather;
    RecyclerView recyclerView;
    List<WeatherResponse> list = new ArrayList<>();
    WeatherAdapter weatherAdapter;

    SwitchCompat switchTheme;
    SharedPreferences prefs;
    SwitchCompat switchNotify;

    String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    String API_KEY = "d4f89a278f449917407116ed2c070315";

    final int WEATHER_ALARM_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCityName = findViewById(R.id.editTextCityName);
        btnSearch = findViewById(R.id.btnSearch);
        tvCityName = findViewById(R.id.textViewCityWeather);
        tvTempWeather = findViewById(R.id.textViewTempWeather);
        tvHumidityWeather = findViewById(R.id.textViewHumidityWeather);
        tvMaxTempWeather = findViewById(R.id.textViewMaxTempWeather);
        tvMinTempWeather = findViewById(R.id.textViewMinTempWeather);
        tvWindWeather = findViewById(R.id.textViewWindWather);
        tvWeatherResult = findViewById(R.id.textViewWeatherConditionWeather);
        imageWeather = findViewById(R.id.imageViewWeather);

        changeMode();
        Notification();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView = findViewById(R.id.recyclerViewCityWeatherList);
        recyclerView.setLayoutManager(layoutManager);
        weatherAdapter = new WeatherAdapter(list);
        recyclerView.setAdapter(weatherAdapter);

        ArrayList<String> cities = new ArrayList<>();

        cities.add("Delhi");
        cities.add("Mumbai");
        cities.add("Chennai");
        cities.add("Jaipur");

//        applyThemeBasedOnLocalTime();

        btnSearch.setOnClickListener(view -> {
            String city = editTextCityName.getText().toString().trim();
            if (!city.isEmpty()) {
                getWeatherData(city);
            } else {
                Toast.makeText(MainActivity.this, "Please enter city name!", Toast.LENGTH_SHORT).show();
            }
        });


        for (String city : cities) {
            weatherReport(city);
        }

        Collections.reverse(cities);


    }

    private void getWeatherData(String city) {

        WeatherApi api = RetrofitClient.getClient(BASE_URL).create(WeatherApi.class);

        Call<WeatherResponse> call = api.getWeather(city, API_KEY, "metric");
//        Call<WeatherResponse> call = apiService.getWeather(cityName,API_KEY,"metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    WeatherResponse data = response.body();

                    String iconCode = "";
                    if (data.getWeather() != null && !data.getWeather().isEmpty()) {
                        iconCode = data.getWeather().get(0).getIcon();  // First weather object ka icon
                    }

                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                    tvCityName.setText(data.getName());
                    tvTempWeather.setText("" + data.getMain().getTemp() + "°C");
                    tvHumidityWeather.setText(": " + data.getMain().getHumidity());
                    tvMaxTempWeather.setText(": " + data.getMain().getTemp_max());
                    tvMinTempWeather.setText(": " + data.getMain().getTemp_min());
                    tvWindWeather.setText("" + data.getWind().getSpeed());

                    String description = "";
                    if (data.getWeather() != null && !data.getWeather().isEmpty()) {
                        description = data.getWeather().get(0).getDescription();
                    }

                    tvWeatherResult.setText(": " + description);

                    Glide.with(imageWeather.getContext())
                            .load(iconUrl)
                            .error(R.drawable.cloud)
                            .into(imageWeather);

                    list.add(response.body());
                    weatherAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainActivity.this, "Moye Moye", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Moye Moye", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void weatherReport(String city) {

        WeatherApi api = RetrofitClient.getClient(BASE_URL).create(WeatherApi.class);

        Call<WeatherResponse> call = api.getWeather(city, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    list.add(response.body());
                    weatherAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainActivity.this, "Moye Moye", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });

    }


    void changeMode() {

        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);

        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        switchTheme = findViewById(R.id.switchTheme);
        switchTheme.setChecked(isDarkMode);

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            recreate();
        });
    }

    void Notification() {
        boolean isNotificationEnabled = prefs.getBoolean("notifications_enabled", false);

        switchNotify = findViewById(R.id.switchNotify);
        switchNotify.setChecked(isNotificationEnabled);

        switchNotify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("notifications_enabled", isChecked);
            editor.apply();

            if (isChecked) {
                scheduleWeatherNotification();
                Toast.makeText(this, "Notification Enabled", Toast.LENGTH_SHORT).show();
            } else {
                cancelWeatherNotification();
                Toast.makeText(this, "Notification Disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scheduleWeatherNotification() {
        Intent intent = new Intent(this, WeatherNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, WEATHER_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long intervalMillis = 60000;
        long triggerAtMillis = Calendar.getInstance().getTimeInMillis() + intervalMillis;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);

    }

    private void cancelWeatherNotification() {
        Intent intent = new Intent(this, WeatherNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, WEATHER_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

//    private void applyThemeBasedOnLocalTime() {
//
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format: 0-23
//
//        int currentMode = AppCompatDelegate.getDefaultNightMode();
//
//        if (hour >= 6 && hour < 18) { // 6:00 AM to 5:59 PM
//            if (currentMode != AppCompatDelegate.MODE_NIGHT_NO) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                recreate();
//            }
//        } else { // 6:00 PM to 5:59 AM
//            if (currentMode != AppCompatDelegate.MODE_NIGHT_YES) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                recreate();
//            }
//        }
//    }


}































/*

//                    double temp = data.getMain().getTemp();

//                    if (temp < 0) {
//                        imageWeather.setImageResource(R.drawable.snow);
//                        tvWeatherResult.setText("Very Cold");
//                    } else if (temp >= 1 && temp <= 10) {
//                        imageWeather.setImageResource(R.drawable.cloud);
//                        tvWeatherResult.setText("Cold");
//                    } else if (temp >= 11 && temp <= 20) {
//                        imageWeather.setImageResource(R.drawable.cloudy);
//                        tvWeatherResult.setText("");
//                    } else if (temp >= 21 && temp <= 25) {
//                        imageWeather.setImageResource(R.drawable.fullmoon);
//                        tvWeatherResult.setText("Mild Warm");
//                    } else if (temp >= 26 && temp <= 30) {
//                        imageWeather.setImageResource(R.drawable.weathericon);
//                        tvWeatherResult.setText("Warm");
//                    } else if (temp >= 31 && temp <= 35) {
//                        imageWeather.setImageResource(R.drawable.fullmoon);
//                        tvWeatherResult.setText("Hot");
//                    } else if (temp >= 36 && temp <= 40) {
//                        tvWeatherResult.setText("Very Hot");
//                        imageWeather.setImageResource(R.drawable.cloudy);
//                    } else {
//                        imageWeather.setImageResource(R.drawable.sun);
//                        tvWeatherResult.setText("Extreme Hot");
//                    }

 */