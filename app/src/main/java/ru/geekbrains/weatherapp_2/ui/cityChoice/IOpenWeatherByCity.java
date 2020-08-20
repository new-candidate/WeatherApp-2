package ru.geekbrains.weatherapp_2.ui.cityChoice;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.weatherapp_2.forecastmod.ForecastRequestModel;

public interface IOpenWeatherByCity {
    @GET("data/2.5/forecast")
    Call<ForecastRequestModel> loadWeather(@Query("q") String city,
                                           @Query("appid") String keyApi,
                                           @Query("units") String units);
}