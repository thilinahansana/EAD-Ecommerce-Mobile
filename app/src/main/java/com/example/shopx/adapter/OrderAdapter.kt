package com.example.shopx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shopx.R
import com.example.shopx.databinding.OrderItemBinding
import com.example.shopx.model.Order

class OrderAdapter(
    private val context: Context,
    private val orders: List<Order>,
    private val onTrackOrderClick: (Order) -> Unit,
    private val onCancelOrderClick: (String) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(private val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.orderId.text = "Order ID: ${order.orderId}"
            binding.orderStatus.text = "${order.status}"

            binding.trackOrderButton.setOnClickListener {
                onTrackOrderClick(order)
            }

            // Setting text color based on order status
            when (order.status?.lowercase()) {
                "pending" -> binding.orderStatus.setTextColor(ContextCompat.getColor(context, R.color.blue))
                "partially-delivered" -> binding.orderStatus.setTextColor(ContextCompat.getColor(context, R.color.light_green))
                "delivered" -> binding.orderStatus.setTextColor(ContextCompat.getColor(context, R.color.dark_green))
                "canceled" -> binding.orderStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
                else -> binding.orderStatus.setTextColor(ContextCompat.getColor(context, R.color.headingclr))
            }

            // Set visibility and click listener for cancel order button
            binding.cancelOrderButton.apply {
                visibility = if (order.status?.lowercase() == "pending") View.VISIBLE else View.GONE
                setOnClickListener {
                    onCancelOrderClick(order.orderId)
                }
            }
        }
    }
}
