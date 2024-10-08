package com.example.shopx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopx.adapter.OrderAdapter
import com.example.shopx.databinding.ActivityOrderBinding
import com.example.shopx.dialog.CancellationDialog
import com.example.shopx.model.Order
import com.example.shopx.service.ApiService
import com.example.shopx.service.RetrofitInstance
import com.example.shopx.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var apiService: ApiService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitInstance.api
        sessionManager = SessionManager(this)



        // Setup RecyclerView
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(this)


        // Fetch orders from API
        val userId = sessionManager.getUser()// Replace with actual userId
        if (userId != null) {
            fetchOrderHistory(userId.id)
        }

    }

    private fun fetchOrderHistory(userId: String) {
        apiService.getOrderHistory(userId).enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                Log.i("String", "History response: $response")
                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    setupRecyclerView(orders)
                } else {
                    Toast.makeText(this@OrderActivity, "Failed to load order history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Toast.makeText(this@OrderActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(orders: List<Order>) {

        val adapter = OrderAdapter(this, orders,
            { order -> // On track order click
                val intent = Intent(this, OrderTrackingActivity::class.java).apply {
                    putExtra("ORDER_ID", order.orderId)
                    putExtra("ORDER_STATUS", order.status)
                    putExtra("TRACKING_DETAILS", order.note)
                    putExtra("ITEM_LIST" , ArrayList(order.items))
                }
                startActivity(intent)
            },
            { orderId -> // On cancel order click
                showCancellationDialog(orderId)
            }
        )
        binding.ordersRecyclerView.adapter = adapter
    }

    private fun showCancellationDialog(orderId: String) {
        CancellationDialog(this) { note ->
            // After note is entered, call the cancelOrder API
            cancelOrder(orderId, note)
        }.show()
    }

    private fun cancelOrder(orderId: String, requestNote: String) {


        val customerId = sessionManager.getUser()?.id // Replace with actual customerId

        val cancelRequest = mapOf(
            "orderId" to orderId,
            "requestNote" to requestNote,
            "customerId" to customerId
        )

        apiService.cancelOrder(cancelRequest as Map<String, String>).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@OrderActivity, "Order cancelled successfully", Toast.LENGTH_SHORT).show()
                    // Optionally refresh the order list
                    if (customerId != null) {
                        fetchOrderHistory(customerId)
                    }
                } else {
                    Toast.makeText(this@OrderActivity, "Failed to cancel order", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@OrderActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
