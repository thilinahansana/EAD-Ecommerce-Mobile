package com.example.shopx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            binding.orderStatus.text = "Status: ${order.status}"

            binding.trackOrderButton.setOnClickListener {
                onTrackOrderClick(order)
            }

            binding.cancelOrderButton.apply {
                visibility = if (order.status == "Pending") View.VISIBLE else View.GONE
                setOnClickListener {
                    onCancelOrderClick(order.orderId)
                }
            }
        }
    }
}