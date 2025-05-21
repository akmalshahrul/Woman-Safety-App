package com.example.safetyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    private final List<AlertModel> alertList;

    public AlertAdapter(List<AlertModel> alertList) {
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alert, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        AlertModel alert = alertList.get(position);

        String message = alert.getMessage();
        String time = DateFormat.getDateTimeInstance().format(new Date(alert.getTimestamp()));

        holder.alertMessage.setText("📍 " + message);
        holder.alertTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    static class AlertViewHolder extends RecyclerView.ViewHolder {
        TextView alertMessage, alertTime;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            alertMessage = itemView.findViewById(R.id.alertMessage);
            alertTime = itemView.findViewById(R.id.alertTime);
        }
    }
}
