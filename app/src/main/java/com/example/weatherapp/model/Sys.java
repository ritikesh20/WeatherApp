package com.example.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("sunrise")
    public long sunrise;

    @SerializedName("sunset")
    public long sunset;

}
