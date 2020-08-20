package ru.geekbrains.weatherapp_2;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.weatherapp_2.forecastmod.ForecastRequestModel;

public interface IOpenWeather {
    @GET("data/2.5/forecast")
    Call<ForecastRequestModel> loadWeather(@Query("lat") String lat,
                                           @Query("lon") String lon,
                                           @Query("appid") String keyApi,
                                           @Query("units") String units);
}
