package com.example.shopx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopx.model.Order
import com.example.shopx.databinding.OrderItemBinding

class OrderAdapter(
    private val context: Context,
    private val orders: List<Order>,
    private val onTrackOrderClicked: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.binding.orderId.text = order.orderId
        holder.binding.orderDate.text = order.orderDate
        holder.binding.orderStatus.text = order.orderStatus

        // Set click listener for tracking button
        holder.binding.trackOrderButton.setOnClickListener {
            onTrackOrderClicked(order)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}

