package ru.geekbrains.weatherapp_2.forecastmod;

import com.google.gson.annotations.SerializedName;



public class CityRestModel {
    @SerializedName("id") public  long id;
    @SerializedName("name") public String name;
    @SerializedName("coord") public CoordRestModel coordinates;
    @SerializedName("country") public String country;
    @SerializedName("population") public String population;
    @SerializedName("timezone") public int timezone;
    @SerializedName("sunrise") public long sunrise;
    @SerializedName("sunset") public long sunset;
}
