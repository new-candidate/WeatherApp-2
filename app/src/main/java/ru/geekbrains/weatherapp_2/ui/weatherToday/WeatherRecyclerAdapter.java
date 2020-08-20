package ru.geekbrains.weatherapp_2.ui.weatherToday;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import ru.geekbrains.weatherapp_2.IObserver;
import ru.geekbrains.weatherapp_2.R;

public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder> implements IObserver {
    private String[] data;
    private Context context;
    private int selectedPosition = -1;
    private OnItemClickListener itemClickListener;
    private String[] forecastDetails1 = new String[5];
    private String[] forecast = new String[5];

    WeatherRecyclerAdapter(String[] data) {
        this.data = data;
        Publisher publisher = Publisher.getInstance();
        publisher.subscribe(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_list_item,
                parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        setText(holder, position);
        setOnItemClickBehavior(holder, position);
        highlightSelectedPosition(holder, position);
    }
    @Override
    public void errorReceive(String errorMessage) {    }

    @Override
    public void updatePlaceName(String city) {    }

    @Override
    public void updateTemp(String temperature) {   }

    @Override
    public void updateDetailsText(String detailsText) {   }

    @Override
    public void updateIcon(Integer position) {   }

    @Override
    public void updateUpdatedText(String updatedText) {   }

    @Override
    public void updateForecast(String[] forecast, Integer[] position) {
        this.forecast = forecast;
    }

    @Override
    public void updateForecastDetails(String[] forecastDetails) {
         this.forecastDetails1 = forecastDetails;
        for (int n=0; n<forecastDetails1.length; n++) {
            Log.d("forecastDetails_uodate", "updateForecastDetails: " + Arrays.toString(forecastDetails));
        }

    }

        public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private void setText(@NonNull ViewHolder holder, final int position) {
        Log.d(" КОНТЕКСТ", "setText: " + context);
        Log.d(" КОНТЕКСТ", "setText: " + data[0] + data[1] + data[2]+ data[3] + " позиция: " + position);
        holder.listItemTextView.setText(data[position]);
    }

    private void setOnItemClickBehavior(@NonNull ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            Log.d("POSITION", "onClick: " + selectedPosition);
            Log.d("POSITION", "onClick: " + forecastDetails1[position]);
            Log.d("POSITION", "onClick: " + forecastDetails1[2]);

            notifyDataSetChanged();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Forecast Details")

                    .setMessage(forecastDetails1[position])
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setCancelable(false)
                    .setPositiveButton(R.string.button,
                            (dialog, id) -> Log.d("TAG", "onClick: Кнопка нажата"));
            AlertDialog alert = builder.create();
            alert.show();
            Toast.makeText(context, "Диалог открыт", Toast.LENGTH_SHORT).show();
        });
    }


    private void highlightSelectedPosition(@NonNull ViewHolder holder, final int position) {
        if(position == selectedPosition) {
            int color = ContextCompat.getColor(context, R.color.colorPrimaryDark);
            holder.itemView.setBackgroundColor(color);
        } else {
            int color = ContextCompat.getColor(context, android.R.color.transparent);
            holder.itemView.setBackgroundColor(color);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView listItemTextView;
        View itemView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            listItemTextView = itemView.findViewById(R.id.forecast_item);
            itemView.setOnClickListener(v -> {
                if (itemClickListener !=null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
