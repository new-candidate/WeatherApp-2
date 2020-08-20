package ru.geekbrains.weatherapp_2.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface HistoryWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHistoryWeather(HistoryWeather historyWeather);

    @Delete
    void deleteHistoryWeather(HistoryWeather historyWeather);

    @Query("DELETE FROM historyWeather WHERE id = :id")
    void deteleHistoryWeatherById(long id);

    @Query("SELECT DISTINCT * FROM historyWeather")
    List<HistoryWeather> getAllHistoryWeather();

    @Query("SELECT * FROM historyWeather WHERE id = :id")
    HistoryWeather getHistoryWeatherById(long id);

    @Query("SELECT COUNT() FROM historyWeather")
    long getCountHistoryWeather();

    @Query("SELECT id, city_name, temperature FROM historyWeather")
    Cursor getHistoryWeatherCursor();

    @Query("SELECT id, city_name, temperature FROM historyWeather WHERE id = :id")
    Cursor getHistoryWeatherCursor(long id);
}
