package ru.geekbrains.weatherapp_2.forecastmod;

import com.google.gson.annotations.SerializedName;

public class ForecastRequestModel {
    @SerializedName("cod") public int cod;
    @SerializedName("message") public String message;
    @SerializedName("cnt") public String cnt;
    @SerializedName("list") public ForecastListRestModel[] list;
    @SerializedName("city") public CityRestModel city;
}