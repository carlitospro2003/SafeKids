package com.example.safekids.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safekids.R;
import com.example.safekids.models.Notifications;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<Notifications> notificationList;

    public NotificationsAdapter(List<Notifications> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifications, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notifications notification = notificationList.get(position);
        String description = notification.getDescription() != null ? notification.getDescription() : "Sin descripción";
        String lastMessage = notification.getLastMessage() != null ? notification.getLastMessage() : "Sin mensaje reciente";
        Log.d("NotificationsAdapter", "Binding notification ID: " + notification.getId() +
                ", Description: " + description + ", LastMessage: " + lastMessage);
        holder.tvDescription.setText(description);
        holder.tvLastMessage.setText(lastMessage);
        holder.imgNotification.setImageResource(R.drawable.notifications);
    }

    public void updateList(List<Notifications> newList) {
        notificationList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;
        TextView tvLastMessage;
        CircleImageView imgNotification;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvNotificationDescription);
            tvLastMessage = itemView.findViewById(R.id.tvNotificationLastMessage);
            imgNotification = itemView.findViewById(R.id.imgNotifications);
        }
    }
}