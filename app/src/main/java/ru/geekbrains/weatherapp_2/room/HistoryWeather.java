package ru.geekbrains.weatherapp_2.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

    @Entity(indices = {@Index(value = {HistoryWeather.CITY_NAME, HistoryWeather.TEMPERATURE, HistoryWeather.SEARCHDATE}, unique = true)})
    public class HistoryWeather {
        private final static String ID = "id";
        final static String CITY_NAME = "city_name";
        final static String TEMPERATURE = "temperature";
        final static String SEARCHDATE = "search_date";

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = ID)
        public long id;

        @ColumnInfo(name = CITY_NAME)
        public String cityName;

        @ColumnInfo(name = TEMPERATURE)
        public String temperature;

        @ColumnInfo(name = SEARCHDATE)
        public String searchDate;
    }

