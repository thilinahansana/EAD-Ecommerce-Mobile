package com.example.shopx

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopx.adapter.NotificationAdapter
import com.example.shopx.model.NotificationResponse
import com.example.shopx.service.ApiService
import com.example.shopx.service.RetrofitInstance
import com.example.shopx.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationsActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        recyclerView = findViewById(R.id.notificationRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        sessionManager = SessionManager(this)

        apiService = RetrofitInstance.api

        val user = sessionManager.getUser()

        // Fetch notifications
        if (user != null) {
            fetchNotifications(user.id)
        }
    }

    private fun fetchNotifications(userId: String) {
        apiService.getNotifications(userId).enqueue(object : Callback<List<NotificationResponse>> {
            override fun onResponse(
                call: Call<List<NotificationResponse>>,
                response: Response<List<NotificationResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val notifications = response.body()!!
                    adapter = NotificationAdapter(notifications)
                    recyclerView.adapter = adapter
                }

                Log.i("String" , "Heloooooooooo $response")
            }

            override fun onFailure(call: Call<List<NotificationResponse>>, t: Throwable) {
                Toast.makeText(this@NotificationsActivity, "Failed to fetch notifications", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
