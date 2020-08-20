package ru.geekbrains.weatherapp_2;

public interface IObserver {

    void updatePlaceName(String city);
    void updateTemp(String temperature);
    void updateDetailsText(String detailsText);
    void updateIcon(Integer position);
    void updateUpdatedText(String updatedText);
    void updateForecast(String [] forecast, Integer[] position);
    void updateForecastDetails(String [] forecastDetails);
    void errorReceive(String errorMessage);
}
