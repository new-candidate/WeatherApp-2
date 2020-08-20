package ru.geekbrains.weatherapp_2.forecastmod;

import com.google.gson.annotations.SerializedName;


public class ForecastListRestModel  {
    @SerializedName("dt") public long dt;
    @SerializedName("main") public MainRestModel main;
    @SerializedName("weather") public WeatherRestModel[] weather;
    @SerializedName("clouds") public  CloudsRestModel clouds;
    @SerializedName("wind") public WindRestModel wind;
    @SerializedName("rain") public  RainRestModel rain;
    @SerializedName("sys") public  SysRestModel sys;
    @SerializedName("dt_txt") public String dtTxt;
}
