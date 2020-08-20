package ru.geekbrains.weatherapp_2.ui.weatherToday;

import android.util.Log;

import androidx.annotation.NonNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.geekbrains.weatherapp_2.IObserver;
import ru.geekbrains.weatherapp_2.OpenWeatherRepo;
import ru.geekbrains.weatherapp_2.forecastmod.ForecastRequestModel;
import ru.geekbrains.weatherapp_2.ui.cityChoice.OpenWeatherByCityRepo;

class Publisher {
    private static Publisher instance = null;
    private static List<IObserver> observers;


    private Publisher() {
    }
    static Publisher getInstance() {
        Log.d("TAG", "onResponse: Обращение к конструктору");
        if (instance == null) {
            instance = new Publisher();
            observers = new ArrayList<>();
        }
        return instance;
    }

    void subscribe(IObserver observer) {
        observers.add(observer);
    }

    private void notifyTemp(String temperature) {
        for (IObserver observer : observers) {
            observer.updateTemp(temperature);
        }
    }

    private void notifyDetails(String detailsText) {
        for (IObserver observer : observers) {
            observer.updateDetailsText(detailsText);
        }
    }

    private void notifyPlaceName(String cityText) {
        for (IObserver observer : observers) {
            observer.updatePlaceName(cityText);
        }
    }
private void notifyForecastDetails (String[] forecastDetails) {
        for (IObserver observer : observers) {
            observer.updateForecastDetails(forecastDetails);
        }
    }

    private void notifyUpdatedText(String updatedText) {
        for (IObserver observer : observers) {
            observer.updateUpdatedText(updatedText);
        }
    }

    private void notifyForecast(String[] forecast, Integer[] iconT) {
        if (forecast[0] != null) {
            for (IObserver observer : observers) {
                observer.updateForecast(forecast, iconT);
            }
        } else {
            Log.d("ERROR", "notifyForecast: with a null");
        }
    }
    private void notifyIcon(Integer position) {
        for (IObserver observer : observers) {
            observer.updateIcon(position);
        }
    }
    private void notifyError(String errorMessage) {
        for (IObserver observer : observers) {
            observer.errorReceive(errorMessage);
        }
    }
    void updateWeatherDataByCity(final String city) {
        Log.d("TAG", "onResponse: Обращаемся к серверу запрашиаем данные по " + city);
        OpenWeatherByCityRepo.getSingleton().getAPI().loadWeather(city ,
                "762ee61f52313fbd10a4eb54ae4d4de2", "metric")
                .enqueue(new Callback<ForecastRequestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ForecastRequestModel> call,
                                           @NonNull Response<ForecastRequestModel> response) {
                        Log.d("TAG", "onResponse: Получили ответ");
                        if (response.body() != null && response.isSuccessful()) {
                            Log.d("TAG", "onResponse: Ответ не пустой");
                            Log.d("TAG", "onResponse: Ответ такой " + response.body());
                            renderWeatherForecast(response.body());
                        } else {
                            Log.d("TAG", "onResponse: Ответ с ошибкой" + response.code());
                            if(response.code() == 500) {
                                Log.d("TAG", "onResponse: Ответ с ошибкой  Internal Server Error");
                            } else if(response.code() == 401) {
                                Log.d("TAG", "onResponse: Ответ с ошибкой  не авторизованы");
                                notifyError("Ответ с ошибкой  не авторизованы");
                            } else if(response.code() == 404) {
                                Log.d("TAG", "onResponse: Ответ с ошибкой  не авторизованы");
                                notifyError("Не могу найти город с таким названием :(");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ForecastRequestModel> call, Throwable t) {
                        Log.d("TAG", "onFailure: ОШИБКА ! ! !" + t);
                        notifyError("Проверьте подключение к серверу и попробуйте еще раз");
                    }
                });
    }

    void updateWeatherDataByLocation(final String lat, String lon) {
        Log.d("TAG", "onResponse: Обращаемся к серверу запрашиаем данные по " + lat + lon);
        OpenWeatherRepo.getSingleton().getAPI().loadWeather(lat, lon,
                "762ee61f52313fbd10a4eb54ae4d4de2", "metric")
                .enqueue(new Callback<ForecastRequestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ForecastRequestModel> call,
                                           @NonNull Response<ForecastRequestModel> response) {
                        Log.d("TAG", "onResponse: Получили ответ");
                        if (response.body() != null && response.isSuccessful()) {
                            Log.d("TAG", "onResponse: Ответ не пустой");
                            renderWeatherForecast(response.body());
                        } else {
                            Log.d("TAG", "onResponse: Ответ с ошибкой" + response.code());
                            if (response.code() == 500) {
                                Log.d("TAG", "onResponse: Ответ с ошибкой  Internal Server Error");
                            } else if (response.code() == 401) {
                                Log.d("TAG", "onResponse: Ответ с ошибкой  не авторизованы");
                                notifyError("Ответ с ошибкой  не авторизованы");
                            } else if (response.code() == 404) {
                                Log.d("TAG", "onResponse: Ответ с ошибкой  не авторизованы");
                                notifyError("Не могу найти город с таким названием :(");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastRequestModel> call, Throwable t) {
                        Log.d("TAG", "onFailure: ОШИБКА ! ! ! Нет подключения" + t);
                        notifyError("Проверьте подключение к серверу и попробуйте еще раз");
                    }
                });
    }

    private void renderWeatherForecast (ForecastRequestModel model) {
        int j = 0;
        Float[] humidity = new Float[60];
        Integer[] iconT = new Integer[5];
        Float[] temp = new Float[50];
        Date[] mill = new Date[60];
        String[] temperature = new String[60];
        Long[] dt = new Long[60];
        String[] description = new String[60];
        Float[] pressure = new Float[60];
        String[] uk = new String[60];
        String[] day = new String[60];
        String [] forecast = new String[5];
        int cod = model.cod;
        Log.d("cod", "код такой " + cod);
        String message = model.message;
        Log.d("cod", "месадж такой " + message);
        String cnt = model.cnt;
        Log.d("cod", "цнт такой " + cnt);
        dt[0] = model.list[0].dt;
        Log.d("cod", "dt такой " + Arrays.toString(dt));
        temp[0] =  model.list[0].main.temp;
        long sunrise = model.city.sunrise;
        long sunset  = model.city.sunset;
        Date currentDate = new Date();
        String [] detailsForecast = new String[5];
        for (int i = 0; i < 39; i++) {
            temp[i] = model.list[i].main.temp;
            humidity[i] = model.list[i].main.humidity;
            description[i] = model.list[i].weather[0].description;
            pressure[i] = model.list[i].main.pressure;
            dt[i] = model.list[i].dt;
            mill [i] = new Date(dt[i]*1000); // не лишнилий ли это лемент???
            uk[i] = SimpleDateFormat
                    .getTimeInstance(SimpleDateFormat.MEDIUM, Locale.UK)
                    .format(mill[i]);
            Log.d("TAG", "renderWeatherForecast: " + String.valueOf(mill[i]) + "температура: " + temp[i] + ", время: " + String.valueOf(uk[i]));
            SimpleDateFormat dateCompare = new SimpleDateFormat("yyyy.MM.dd");
            if (uk[i].equals("12:00:00") && !dateCompare.format(mill[i]).equals(dateCompare.format(currentDate))) {
                temperature[j] = String.format(Locale.getDefault(), "%.2f", temp[i]);
                iconT[j] = weatherIcon(model.list[i].weather[0].id, sunrise, sunset);
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
                day[j] = dateFormat.format(mill[i]);
                forecast[j] = day[j] + "  " + temperature[j] +"\u2103";
                  detailsForecast[j] = ("Weather forecast on: " + dateCompare.format(mill[j]) + "\n"
                        + "Temperature: " + temperature[j] + "\u2103" + "\n"
                        + description[i].toUpperCase() + "\n"
                        + "Humidity: " + humidity[i] + "%" + "\n"
                        + "Pressure: " + pressure[i] + "hPa");
                Log.d("PRINT_ARR", "1renderWeatherForecast: " + detailsForecast[j] + "position " + j);
                j++;
            }
        }
        String cityName = model.city.name;
        String country = String.valueOf(model.city.country);
        setPlaceName(cityName, country);
        setUpdatedText(currentDate.getTime());
        notifyForecast(forecast, iconT);
        setDetails(description[0], humidity[0], pressure[0]);
        setCurrentTemp(temp[0]);
        Integer position = weatherIcon(model.list[0].weather[0].id,
                model.list[0].sys.sunrise * 1000,
                model.list[0].sys.sunset * 1000);
        notifyIcon(position);
        notifyForecastDetails(detailsForecast);
    }

    private void setPlaceName(String name, String country) {
        String cityText = name.toUpperCase() + ", " + country;
        notifyPlaceName(cityText);
    }

    private void setUpdatedText(long dt) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String updateOn = dateFormat.format(new Date(dt));
        Log.d("TAG", "Получили время обноевлени отдаем дальше " + updateOn);
        notifyUpdatedText(updateOn);
    }

    private void setCurrentTemp(float temp) {
        String temperature = String.format(Locale.getDefault(), "%.2f", temp) + "\u2103";
        notifyTemp(temperature);
    }

    private void setDetails(String description, float humidity, float pressure) {
        String detailsText = description.toUpperCase() + "\n"
                + "Humidity: " + humidity + "%" + "\n"
                + "Pressure: " + pressure + "hPa";
        notifyDetails(detailsText);
    }

    private int weatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        int position = -1;
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                position = 0;
            } else {
                position = 1;
            }
        } else {
            switch (id) {
                case 2: {
                    position = 2;

                    break;
                }
                case 3: {
                    position = 3;
                    break;
                }
                case 5: {
                    position = 4;
                    break;
                }
                case 6: {
                    position = 5;
                    break;
                }
                case 7: {
                    position = 6;
                    break;
                }
                case 8: {
                    position = 7;
                    break;
                }
            }
        }
        return position;
    }
}
