package com.example.shopx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopx.R
import com.example.shopx.model.NotificationResponse


class NotificationAdapter(private val notifications: List<NotificationResponse>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.notificationMessage)
        val dateTextView: TextView = view.findViewById(R.id.notificationDate)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.messageTextView.text = notification.message
        holder.dateTextView.text = notification.sentDate
    }

    override fun getItemCount(): Int = notifications.size
}
