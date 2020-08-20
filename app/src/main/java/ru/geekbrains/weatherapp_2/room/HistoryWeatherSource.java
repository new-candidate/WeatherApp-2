package ru.geekbrains.weatherapp_2.room;

import java.util.List;

public class HistoryWeatherSource {

    private final HistoryWeatherDao historyWeatherDao;
    private List<HistoryWeather> historyWeathers;

    public HistoryWeatherSource(HistoryWeatherDao historyWeatherDao){
        this.historyWeatherDao = historyWeatherDao;
    }

    public List<HistoryWeather> getHistoryWeather(){
        if (historyWeathers == null){
            loadHistoryWeathers();
        }
        return historyWeathers;
    }

    private void loadHistoryWeathers(){
        historyWeathers = historyWeatherDao.getAllHistoryWeather();
    }

    public long getCountHistoryWeather(){
        return historyWeatherDao.getCountHistoryWeather();
    }

    public void addHistoryWeather(HistoryWeather historyWeather){
        historyWeatherDao.insertHistoryWeather(historyWeather);
        loadHistoryWeathers();
    }
}
