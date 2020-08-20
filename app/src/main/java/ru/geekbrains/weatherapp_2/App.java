package ru.geekbrains.weatherapp_2;

import android.app.Application;
import androidx.room.Room;
import ru.geekbrains.weatherapp_2.room.HistoryWeatherDao;
import ru.geekbrains.weatherapp_2.room.HistoryWeatherDataBase;


public class App extends Application {
    private static App instance;
    private HistoryWeatherDataBase db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = Room.databaseBuilder(
                getApplicationContext(),
                HistoryWeatherDataBase.class,
                "weather_database")
//                .allowMainThreadQueries() //Только для примеров и тестирования.
                .build();
    }
    public HistoryWeatherDao getHistoryWeatherDao() {
        return db.getHistoryWeatherDao();
    }
}
