package ru.geekbrains.weatherapp_2.ui.historyWeather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.geekbrains.weatherapp_2.App;
import ru.geekbrains.weatherapp_2.R;
import ru.geekbrains.weatherapp_2.room.HistoryWeatherDao;
import ru.geekbrains.weatherapp_2.room.HistoryWeatherSource;

public class HistoryWeatherFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slideshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        HistoryWeatherSource historyWeatherSource;
        HistoryWeatherRecyclerAdapter adapter;
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        HistoryWeatherDao historyWeatherDao = App
                .getInstance()
                .getHistoryWeatherDao();
        historyWeatherSource = new HistoryWeatherSource(historyWeatherDao);
        adapter = new HistoryWeatherRecyclerAdapter(historyWeatherSource, this);
        recyclerView.setAdapter(adapter);
    }
}
