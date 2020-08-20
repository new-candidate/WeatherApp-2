package ru.geekbrains.weatherapp_2.ui.cityChoice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OpenWeatherByCityRepo {
    private static OpenWeatherByCityRepo singleton = null;
    private IOpenWeatherByCity API;

    private OpenWeatherByCityRepo() {
        API = createAdapter();
    }

    public static OpenWeatherByCityRepo getSingleton() {
        if (singleton == null) {
            singleton = new OpenWeatherByCityRepo();
        }
        return singleton;
    }

    public IOpenWeatherByCity getAPI() {
        return API;
    }

    private IOpenWeatherByCity createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return adapter.create(IOpenWeatherByCity.class);
    }
}
