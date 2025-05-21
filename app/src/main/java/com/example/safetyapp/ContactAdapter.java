package com.example.safetyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    public interface OnCallClick {
        void onCall(EmergencyContactModel contact);
    }

    public interface OnDeleteClick {
        void onDelete(EmergencyContactModel contact);
    }

    private final List<EmergencyContactModel> contactList;
    private final OnCallClick callCallback;
    private final OnDeleteClick deleteCallback;

    public ContactAdapter(List<EmergencyContactModel> contactList,
                          OnCallClick callCallback,
                          OnDeleteClick deleteCallback) {
        this.contactList = contactList;
        this.callCallback = callCallback;
        this.deleteCallback = deleteCallback;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        EmergencyContactModel contact = contactList.get(position);
        holder.name.setText(contact.name);
        holder.phone.setText(contact.phone);

        holder.btnCall.setOnClickListener(v -> callCallback.onCall(contact));
        holder.btnDelete.setOnClickListener(v -> deleteCallback.onDelete(contact));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;
        Button btnCall, btnDelete;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contactName);
            phone = itemView.findViewById(R.id.contactPhone);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
