package com.example.weatherapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.model.WeatherResponse;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    List<WeatherResponse> weatherList;

    public WeatherAdapter(List<WeatherResponse> weatherList) {
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_ui, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WeatherResponse data = weatherList.get(position);

        String iconCode = "";
        if (data.getWeather() != null && !data.getWeather().isEmpty()) {
            iconCode = data.getWeather().get(0).getIcon();
        }

        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

        holder.ctName.setText("" + data.getName());
        holder.ctTemp.setText("" + data.getMain().getTemp() + "°");

        Glide.with(holder.itemView.getContext())
                .load(iconUrl)
                .error(R.drawable.cloud)
                .into(holder.imageWeatherCondition);
    }

    @Override
    public int getItemCount() {

        return weatherList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageWeatherCondition;
        TextView ctName, ctTemp, textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageWeatherCondition = itemView.findViewById(R.id.cityWeatherImage);
            ctName = itemView.findViewById(R.id.cityNameList);
            ctTemp = itemView.findViewById(R.id.cityTempList);


        }
    }
}
