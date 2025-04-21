package com.example.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {

    private MainX main;
    private String name;
    private Wind wind;
    private List<Weather> weather;

    @SerializedName("sys")
    private Sys sys;


    public MainX getMain() {
        return main;
    }

    public Sys getSys() {
        return sys;
    }

    public String getName() {
        return name;
    }

    public Wind getWind() {
        return wind;
    }

    public List<Weather> getWeather() {
        return weather;
    }

}
