package ru.geekbrains.weatherapp_2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ru.geekbrains.weatherapp_2.ui.cityChoice.CityChoiceFragment;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {
    private ArrayList<String> cities;
    private CityChoiceFragment activity;
    private OnItemClickListener itemClickListener;

    public MenuListAdapter(ArrayList<String> cities, CityChoiceFragment activity) {
        this.activity = activity;
        if (cities != null) {
            this.cities = cities;
        } else {
            this.cities = new ArrayList<>();
        }
    }

    public void addItem(String newCity) {
        this.cities.add(0, newCity);
        notifyItemInserted(0);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_choice_list,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = cities.get(position);
        holder.textView.setText(text);
        activity.registerForContextMenu(holder.textView);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}

