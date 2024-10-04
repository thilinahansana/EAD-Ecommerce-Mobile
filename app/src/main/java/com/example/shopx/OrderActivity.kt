package com.example.shopx

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopx.adapter.OrderAdapter
import com.example.shopx.databinding.ActivityOrderBinding
import com.example.shopx.model.Order

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sample order data
        val orders = listOf(
            Order("12345", "01-01-2024", "Shipped", "Your order is on the way."),
            Order("67890", "02-01-2024", "Delivered", "Your order was delivered on 03-01-2024."),
            Order("54321", "03-01-2024", "Processing", "Your order is being prepared.")
        )

        // Set up the RecyclerView
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.ordersRecyclerView.adapter = OrderAdapter(this, orders) { order ->
            // Handle track order button click
            val intent = Intent(this, OrderTrackingActivity::class.java)
            intent.putExtra("orderId", order.orderId)
            intent.putExtra("orderStatus", order.orderStatus)
            intent.putExtra("trackingDetails", order.trackingDetails)
            startActivity(intent)
        }
    }
}
