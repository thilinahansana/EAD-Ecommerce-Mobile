package com.example.shopx

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopx.adapter.OrderItemAdapter
import com.example.shopx.databinding.ActivityOrderTrakingBinding
import com.example.shopx.model.OrderItem
import com.example.shopx.service.ApiService
import com.example.shopx.service.RetrofitInstance
import com.example.shopx.session.SessionManager

class OrderTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderTrakingBinding
    private lateinit var orderItemAdapter: OrderItemAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrakingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        apiService = RetrofitInstance.api

        // Retrieve order tracking details from the intent
        val orderId = intent.getStringExtra("ORDER_ID")
        val orderStatus = intent.getStringExtra("ORDER_STATUS")
        val trackingDetails = intent.getStringExtra("TRACKING_DETAILS")
        @Suppress("DEPRECATION")
        val itemList = intent.getSerializableExtra("ITEM_LIST") as? ArrayList<OrderItem>

        Log.i("OrderTrackingActivity", "Order Items: $itemList")

        // Set the details in the UI
        binding.orderIdText.text = getString(R.string.order_id_format, orderId)
        binding.orderStatusText.text = getString(R.string.order_status_format, orderStatus)
        binding.trackingDetailsText.text = trackingDetails ?: getString(R.string.tracking_details_unavailable)

        // Set up RecyclerView with order items
        setupRecyclerView(itemList)
    }

    private fun setupRecyclerView(itemList: List<OrderItem>?) {
        itemList?.let {
            binding.itemsRecyclerView.layoutManager = LinearLayoutManager(this)
            // Pass `this` as the Context and `itemList` as the list of items

            orderItemAdapter = OrderItemAdapter(this, it ,apiService , sessionManager)
            binding.itemsRecyclerView.adapter = orderItemAdapter
        }
    }
}
