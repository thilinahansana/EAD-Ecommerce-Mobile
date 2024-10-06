package com.example.shopx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopx.databinding.ItemLayoutBinding
import com.example.shopx.model.OrderItem

class OrderItemAdapter(
    private val context: Context,
    private val items: List<OrderItem>
) : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class OrderItemViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.itemName.text = "${item.productName} (${item.productName})"
            binding.itemQuantity.text = "Quantity: ${item.quantity}"
            binding.itemPrice.text = "Price: $${item.price}"

            binding.rateItemButton.setOnClickListener {
                // Implement rating functionality
            }

            binding.viewVendorButton.setOnClickListener {
                // Implement view vendor functionality
            }
        }
    }
}