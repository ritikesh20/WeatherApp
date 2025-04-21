package com.example.weatherapp;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.weatherapp.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.Response;

public class WeatherWorker extends Worker {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your API key

    public WeatherWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String city = getInputData().getString("CITY");
        WeatherApi api = RetrofitClient.getClient("").create(WeatherApi.class);

        try {
            Call<WeatherResponse> call = api.getWeather(city, API_KEY, "metric");
            Response<WeatherResponse> responseResponse = call.execute();

            if (responseResponse.isSuccessful() && responseResponse.body() != null){
                WeatherResponse weather = responseResponse.body();

                return Result.success();
            } else {
                return Result.retry();
            }



        } catch (Exception e) {
            return Result.retry();
        }

    }
}
