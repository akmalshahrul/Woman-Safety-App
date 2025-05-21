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

public class PushAlertAdapter extends RecyclerView.Adapter<PushAlertAdapter.PushAlertViewHolder> {

    private final List<PushAlertModel> alertList;

    public PushAlertAdapter(List<PushAlertModel> alertList) {
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public PushAlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_push_alert, parent, false); // Reuse item_alert.xml
        return new PushAlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PushAlertViewHolder holder, int position) {
        PushAlertModel alert = alertList.get(position);

        holder.alertMessage.setText("📍 " + alert.message);
        String formattedTime = DateFormat.getDateTimeInstance().format(new Date(alert.timestamp));
        holder.alertTime.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    static class PushAlertViewHolder extends RecyclerView.ViewHolder {
        TextView alertMessage, alertTime;

        public PushAlertViewHolder(@NonNull View itemView) {
            super(itemView);
            alertMessage = itemView.findViewById(R.id.alertMessage);
            alertTime = itemView.findViewById(R.id.alertTime);
        }
    }
}
