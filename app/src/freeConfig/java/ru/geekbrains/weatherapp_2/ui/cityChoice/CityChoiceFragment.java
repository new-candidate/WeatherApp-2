package ru.geekbrains.weatherapp_2.ui.cityChoice;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ru.geekbrains.weatherapp_2.MenuListAdapter;
import ru.geekbrains.weatherapp_2.R;


public class CityChoiceFragment extends Fragment {
    private MenuListAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        buttonByCityInit();
        if (getArguments() != null) {
            String city = getArguments().getString("back_city_key");
            EditText myCityName = requireView().findViewById(R.id.elementadd);
            myCityName.setText(city);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initList() {
        ArrayList<String> cities = new ArrayList<>();
        cities.add("Moscow");
        cities.add("Berlin");
        cities.add("Paris");
        cities.add("Minsk");
        cities.add("Barcelona");
        cities.add("Shtutgard");
        cities.add("Franfurt");
        cities.add("Rome");
        cities.add("New-York");
        cities.add("Washington");
        cities.add("Madrid");
        cities.add("Monako");
        cities.add("Mehico");
        cities.add("Amsterdam");

        adapter = new MenuListAdapter(cities, this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener((view, position) -> {
            EditText myCityName = requireView().findViewById(R.id.elementadd);
            String cityName1 = String.format("%s", ((TextView) view).getText());
            myCityName.setText(cityName1);
            navigateToForecast(view);
        });
    }

    private void buttonByCityInit() {
        Button okButton = requireView().findViewById(R.id.to_weather_city);
        okButton.setOnClickListener(this::navigateToForecast);
    }
    private void navigateToForecast(View view) {
        EditText myCityName = requireView().findViewById(R.id.elementadd);
        final String cityName = myCityName.getText().toString();
        adapter.addItem(cityName);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        SharedPreferences.Editor cityAdder = preferences.edit();
        cityAdder.putString("textKey", cityName);
        cityAdder.apply();
        Bundle bundle = new Bundle();
        bundle.putString("city_key", cityName);
        Navigation.findNavController(view).navigate(R.id.confirmationAction, bundle);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }
}



