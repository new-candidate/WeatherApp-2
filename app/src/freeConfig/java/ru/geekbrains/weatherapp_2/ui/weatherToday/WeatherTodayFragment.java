package ru.geekbrains.weatherapp_2.ui.weatherToday;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import ru.geekbrains.weatherapp_2.App;
import ru.geekbrains.weatherapp_2.IObserver;
import ru.geekbrains.weatherapp_2.R;
import ru.geekbrains.weatherapp_2.room.HistoryWeather;
import ru.geekbrains.weatherapp_2.room.HistoryWeatherDao;
import ru.geekbrains.weatherapp_2.room.HistoryWeatherSource;


public class WeatherTodayFragment extends Fragment implements IObserver {

    private Typeface weatherFont;
    private TextView cityTextView;
    private TextView detailsTextView;
    private TextView updatedTextView;
    private static TextView currentTemperatureTextView;
    private ImageView weatherIconTextView;
    private String temperature;
    private String detailsText;
    private Button openUrlBtn;
    private String city;
    private String [] icon = new String [5];
    private String updatedText;
    private HistoryWeatherSource historyWeatherSource;
    private final static String TAG = "LOCATION";
     private String [] newForecast = {"loading","loading","loading","loading","loading"};
    private RecyclerView recyclerView;
    private WeatherRecyclerAdapter adapter;
    private ProgressBar prgBar;
//    ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
    public WeatherTodayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    private void initViews(View view) {
        cityTextView = requireView().findViewById(R.id.curent_city);
        detailsTextView = requireView().findViewById(R.id.details_field);
        updatedTextView = requireView().findViewById(R.id.updated_field);
        currentTemperatureTextView = requireView().findViewById(R.id.current_temperature_field);
        weatherIconTextView = requireView().findViewById(R.id.weather_icon);
        initRecyclerView();
    }

    private void initFonts() {
        weatherFont = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/weather.ttf");
        cityTextView = requireView().findViewById(R.id.curent_city);
    }
    private void initRecyclerView() {
        WeatherRecyclerAdapter adapter = new WeatherRecyclerAdapter(newForecast);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = requireView().findViewById(R.id.recycler_id);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void addHistoryString() {
        Log.d(TAG, "addHistoryString: Метод addHistory");
            if (city == null || temperature == null || updatedText == null) {
                Log.d(TAG, "addHistoryString: Попытька добавить пустой обьект " + city + " "+ temperature+ " " + updatedText);
            } else {
                AsyncTask.execute(() -> {
                    HistoryWeatherDao historyWeatherDao = App
                            .getInstance()
                            .getHistoryWeatherDao();
                    historyWeatherSource = new HistoryWeatherSource(historyWeatherDao);
                    HistoryWeather historyWeather = new HistoryWeather();
                    historyWeather.cityName = city;
                    historyWeather.temperature = temperature;
                    Log.d(TAG, "addHistoryString: Метод addHistory, пытаемся добавить город " + city);
                    historyWeather.searchDate = updatedText;
                    historyWeatherSource.addHistoryWeather(historyWeather);
                });
            }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initFonts();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        assert getArguments() != null;
        city = getArguments().getString("city_key");
        String cityFromPreferences = preferences.getString("textKey", null);
        String lat = getArguments().getString("lat_key");
        Log.d(TAG, "onViewCreated: получили в дугом фрагменте такой lat" + lat);
        String lng = getArguments().getString("lng_key");
        Log.d(TAG, "onViewCreated: получили в другом фрагменте такой lng " + lng);
        if (city != null) {
            updateDataByCity(city);
        } else if (cityFromPreferences != null) {
            cityTextView.setText(cityFromPreferences);
            updateDataByCity(cityFromPreferences);
        } else {
            Navigation.findNavController(view).navigate(R.id.nav_home);
        }
    }

    private void updateDataByCity(String city) {
        cityTextView.setText(city);
        Log.d("TAG", "onViewCreated: Добавили в базу город " + city);
        Publisher publisher = Publisher.getInstance();
        publisher.updateWeatherDataByCity(city);
        publisher.subscribe(this);
        Log.d("TAG", "onResponse: Обращение к конструктору в onCreate 2!, отдаем город " + city);
    }

    @Override
    public void updateTemp(String temperature) {
        this.temperature = temperature;
        currentTemperatureTextView.setText(temperature);
        Log.d("TAG", "updateText: " + temperature);

    }

    @Override
    public void updateDetailsText(String detailsText) {
        this.detailsText = detailsText;
        detailsTextView.setText(detailsText);
    }

    @Override
    public void errorReceive(String errorMessage) {
        Toast toast = Toast.makeText(getContext(),
                errorMessage, Toast.LENGTH_LONG);
        toast.show();
        navigateToHome();
    }

    private void navigateToHome() {
        Log.d(TAG, "navigateToHome: " + getView());
        View fragmentView = getView();
        if (fragmentView != null) {
            String textCity = cityTextView.getText().toString();
            Log.d(TAG, "navigateToHome: должно быть не null " + getView());
            Log.d(TAG, "navigateToHome: Такой город отддадим  " + textCity);
            Bundle weatherBundle = new Bundle();
            weatherBundle.putString("back_city_key", textCity);
            Navigation.findNavController(fragmentView).navigate(R.id.action_error_nav_home, weatherBundle);
        }
    }

    @Override
    public void updateIcon(Integer position) {
        String todayIcon = setIconPicasso(position);
        loadImageWithPicasso(todayIcon);
    }

    @Override
    public void updateUpdatedText(String updatedText) {
        this.updatedText = updatedText;
        updatedTextView.setText(updatedText);
        Log.d("TAG", "onResponse: Обращение к конструктору в onCreate 3!, отдаем город " + city);
        addHistoryString();
    }

    @Override
    public void updateForecast(String[] forecast, Integer[] iconT) {

        for (int i = 0; i < forecast.length; i++) {
            icon[i] = setIcons(iconT[i]);
        }
        newForecast = Arrays.copyOf(forecast, forecast.length);
        for (int i = 0; i < forecast.length; i++) {
            if (icon[i]==null) {
                newForecast[i] = " ";
            } else {
                newForecast[i] = newForecast[i] + icon[i];
            }
        }
        Log.d("ICON", "updateForecast: " + newForecast[0]);
        if (adapter == null) {
            adapter = new WeatherRecyclerAdapter(newForecast);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        for (int i = 0; i < forecast.length; i++) {
            Log.d("InFragment", "updateForecast: " + newForecast[i]);
        }
    }

    @Override
    public void updateForecastDetails(String[] forecastDetails) {

    }

    @Override
    public void updatePlaceName(String city) {
        this.city = city;
        cityTextView.setText(city);
    }

    private String setIcons(Integer position) {
        Log.d("Позиция Иконки", "setIcons: " + position);
        String content = null;
        if (position == null) {
            return content = null;
        }
        if (isAdded()) {
            switch (position) {
                case 0:
                case 1: {
                    content = getString(R.string.h_weather_sunny);
                    return content;
                }
                case 2: {
                    content = getString(R.string.h_weather_thunder);
                    return content;
                }
                case 3: {
                    content = getString(R.string.h_weather_drizzle);
                    return content;
                }
                case 4: {
                    content = getString(R.string.h_weather_rainy);
                    return content;
                }
                case 5: {
                    content = getString(R.string.h_weather_snowy);
                    return content;
                }
                case 6: {
                    content = getString(R.string.h_weather_foggy);
                    return content;
                }
                case 7: {
                    content = getString(R.string.h_weather_cloudy);
                    return content;
                }

            }
        }
        Log.d("ICON-MINI", "Иконка такая отдалась: " + content);
        return content;
    }

    private String setIconPicasso(Integer position) {

        String content = null;
        if (isAdded()) {
            switch (position) {
                case 0: {
                    content = getString(R.string.weather_sunny);
                    return content;
                }
                case 1: {
                    content = getString(R.string.weather_clear_night);
                    return content;
                }
                case 2: {
                    content = getString(R.string.weather_thunder);
                    return content;
                }
                case 3: {
                    content = getString(R.string.weather_drizzle);
                    return content;
                }
                case 4: {
                    content = getString(R.string.weather_rainy);
                    return content;
                }
                case 5: {
                    content = getString(R.string.weather_snowy);
                    return content;
                }
                case 6: {
                    content = getString(R.string.weather_foggy);
                    return content;
                }
                case 7: {
                    content = getString(R.string.weather_cloudy);
                    return content;
                }
            }
        }
        return content;
    }

    private void loadImageWithPicasso(String path) {
        Picasso.get()
                .load(path)
                .placeholder(R.drawable.ic_launcher_background)
                .into(weatherIconTextView);
        Log.d("ICON", "loadImageWithPicasso: " + path);
    }
}


