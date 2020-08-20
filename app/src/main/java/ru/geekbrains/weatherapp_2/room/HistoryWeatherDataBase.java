package ru.geekbrains.weatherapp_2.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {HistoryWeather.class}, version = 2)
public abstract class HistoryWeatherDataBase extends RoomDatabase {
    public abstract HistoryWeatherDao getHistoryWeatherDao();
}