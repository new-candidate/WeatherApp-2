package ru.geekbrains.weatherapp_2.ui.weatherToday;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.geekbrains.weatherapp_2.IObserver;
import ru.geekbrains.weatherapp_2.OpenWeatherRepo;
import ru.geekbrains.weatherapp_2.R;
//import ru.geekbrains.weatherapp_2.entities.WeatherRequestRestModel;
import ru.geekbrains.weatherapp_2.forecastmod.ForecastRequestModel;
import ru.geekbrains.weatherapp_2.ui.cityChoice.OpenWeatherByCityRepo;

public class PublisherByCity {
//    private static PublisherByCity instance = null;
//    private static List<IObserver> observers;
//    private final Handler handler = new Handler();
//    Context mAppContext;
//
//    private PublisherByCity (String city) {
//        updateWeatherData(city);
//    }
//
//    static PublisherByCity getInstance(String city) {
//        Log.d("TAG", "onResponse: Обращение к конструктору");
//        if (instance == null) {
//            instance = new PublisherByCity(city);
//            observers = new ArrayList<>();
//        }
//        return instance;
//    }
//
//    void subscribe(IObserver observer) {
//        observers.add(observer);
//    }
//
//    private void notifyTemp(String temperature) {
//        for (IObserver observer : observers) {
//            observer.updateTemp(temperature);
//        }
//    }
//
//    private void notifyDetails(String detailsText) {
//        for (IObserver observer : observers) {
//            observer.updateDetailsText(detailsText);
//        }
//    }
//    private void notifyPlaceName(String cityText) {
//        for (IObserver observer : observers) {
//            observer.updatePlaceName(cityText);
//        }
//    }
//
//    private void notifyUpdatedText(String updatedText) {
//        for (IObserver observer : observers) {
//            observer.updateUpdatedText(updatedText);
//        }
//    }
//    private void notifyIcon(int position) {
//        for (IObserver observer : observers) {
//            observer.updateIcon(position);
//        }
//    }
//
//    void updateWeatherData(final String city) {
//        Log.d("TAG", "onResponse: Обращаемся к серверу запрашиаем данные по " + city);
//        OpenWeatherByCityRepo.getSingleton().getAPI().loadWeather(city ,
//                "762ee61f52313fbd10a4eb54ae4d4de2", "metric")
//                .enqueue(new Callback<ForecastRequestModel>() {
//                    @Override
//                    public void onResponse(@NonNull Call<ForecastRequestModel> call,
//                                           @NonNull Response<ForecastRequestModel> response) {
//                        Log.d("TAG", "onResponse: Получили ответ");
//                        if (response.body() != null && response.isSuccessful()) {
//                            Log.d("TAG", "onResponse: Ответ не пустой");
//                            renderWeatherForecast(response.body());
//                        } else {
//                            Log.d("TAG", "onResponse: Ответ с ошибкой" + response.code());
//                            //Похоже, код у нас не в диапазоне [200..300) и случилась ошибка
//                            //обрабатываем ее
//                            if(response.code() == 500) {
//                                Log.d("TAG", "onResponse: Ответ с ошибкой  Internal Server Error");
//                                //ой, случился Internal Server Error. Решаем проблему
//                            } else if(response.code() == 401) {
//                                Log.d("TAG", "onResponse: Ответ с ошибкой  не авторизованы");
//                                //не авторизованы, что-то с этим делаем.
//                                //например, открываем страницу с логинкой
//                            }// и так далее
//                        }
//                    }
//                    //сбой при интернет подключении
//                    @Override
//                    public void onFailure(Call<ForecastRequestModel> call, Throwable t) {
//
//                    }
//                });
//    }
//    private void renderWeatherForecast (ForecastRequestModel model) {
//        String[] humidity = new String[50];
//        String[] rain_description = new String[50];
//        String[] icon = new String[50];
//        String[] time = new String[50];
//        Float[] temp = new Float[50];
//        int cod = model.cod;
//        Log.d("cod", "код такой " + cod);
//
//        for (int i = 0; i < 4; i++) {
//            temp[i] = model.list[i].main.temp;
//            humidity[i] = String.valueOf(model.list[i].main.humidity);
//            rain_description[i] = String.valueOf(model.list[i].weather[0].description);
////            icon[i] = String.valueOf(response.getList().get(i).getWeather().get(0).getIcon());
//            time[i] = String.valueOf(model.list[i].dt);
//
//            Log.w("humidity", humidity[i]);
//            Log.w("rain_description", rain_description[i]);
//            Log.w("icon", icon[i]);
//            Log.w("time", time[i]);
//        }
//        String cityName = model.city.name;
//        String country = String.valueOf(model.city.country);
//        setPlaceName(cityName, country);
//        setCurrentTemp(temp[3]);
//    }
////    private void renderWeather(WeatherRequestRestModel model) {
////        Log.d("TAG", "onResponse: Дошли до renderWeather");
////        setPlaceName(model.name, model.sys.country);
////        setDetails(model.weather[0].description, model.main.humidity, model.main.pressure);
////        setCurrentTemp(model.main.temp);
////        setUpdatedText(model.dt);
////        setWeatherIcon(model.weather[0].id,
////                model.sys.sunrise * 1000,
////                model.sys.sunset * 1000);
////    }
//    private void setUpdatedText(long dt) {
//        DateFormat dateFormat = DateFormat.getDateTimeInstance();
//        String updateOn = dateFormat.format(new Date(dt * 1000));
//        Log.d("TAG", "Получили время обноевлени отдаем дальше " + updateOn);
//        notifyUpdatedText(updateOn);
//    }
//    private void setCurrentTemp(float temp)  {
//        String temperature = String.format(Locale.getDefault(), "%.2f", temp) + "\u2103";
//        notifyTemp(temperature);
//    }
//
//    private void setDetails(String description, float humidity, float pressure)  {
//        String detailsText = description.toUpperCase() + "\n"
//                + "Humidity: " + humidity + "%" + "\n"
//                + "Pressure: " + pressure + "hPa";
//        notifyDetails(detailsText);
//    }
//    private void setPlaceName(String name, String country) {
//        String cityText = name.toUpperCase() + ", " + country;
//        notifyPlaceName(cityText);
//    }
//    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
//        int id = actualId / 100;
//        int position = -1;
//        if (actualId == 800) {
//            long currentTime = new Date().getTime();
//            if (currentTime >= sunrise && currentTime < sunset) {
//                position = 0;
//            } else {
//                position = 1;
//            }
//        } else {
//            switch (id) {
//                case 2: {
//                    position = 2;
//                    break;
//                }
//                case 3: {
//                    position = 3;
//                    break;
//                }
//                case 5: {
//                    position = 4;
//                    break;
//                }
//                case 6: {
//                    position = 5;
//                    break;
//                }
//                case 7: {
//                    position = 6;
//                    break;
//                }
//                case 8: {
//                    position = 7;
//                    break;
//                }
//            }
//        }
//        notifyIcon(position);
//    }
}
