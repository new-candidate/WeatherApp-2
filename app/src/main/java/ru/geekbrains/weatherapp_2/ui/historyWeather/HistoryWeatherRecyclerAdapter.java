package ru.geekbrains.weatherapp_2.ui.historyWeather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import ru.geekbrains.weatherapp_2.R;
import ru.geekbrains.weatherapp_2.room.HistoryWeather;
import ru.geekbrains.weatherapp_2.room.HistoryWeatherSource;


public class HistoryWeatherRecyclerAdapter extends RecyclerView.Adapter<HistoryWeatherRecyclerAdapter.ViewHolder> {
    private HistoryWeatherFragment activity;
    private HistoryWeatherSource dataSource;

    HistoryWeatherRecyclerAdapter(HistoryWeatherSource dataSource, HistoryWeatherFragment activity){
        this.dataSource = dataSource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_weather, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Заполнение данными записи на экране
        List<HistoryWeather> historyWeathers = dataSource.getHistoryWeather();
        HistoryWeather historyWeather = historyWeathers.get(position);
        holder.historyWeatherTemperature.setText(historyWeather.searchDate);
        String firstLine = historyWeather.cityName  + " " + historyWeather.temperature;
        holder.historyWeatherCityName.setText(firstLine);
        // регистрируем контекстное меню
        if (activity != null){
            activity.registerForContextMenu(holder.cardView);
        }
    }

    @Override
    public int getItemCount() {
        return (int) dataSource.getCountHistoryWeather();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView historyWeatherCityName;
        TextView historyWeatherTemperature;
        View cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            historyWeatherTemperature = cardView.findViewById(R.id.textHistoryWeatherTemperature);
            historyWeatherCityName = cardView.findViewById(R.id.textHistoryWeatherCityName);
        }
    }
}
