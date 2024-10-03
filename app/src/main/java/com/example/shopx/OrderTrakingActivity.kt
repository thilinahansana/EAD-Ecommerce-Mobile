package com.example.shopx

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.example.shopx.databinding.ActivityOrderTrakingBinding

class OrderTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderTrakingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrakingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve order tracking details from the intent
        val orderId = intent.getStringExtra("orderId")
        val orderStatus = intent.getStringExtra("orderStatus")
        val trackingDetails = intent.getStringExtra("trackingDetails")

        // Set the details in the UI
        binding.orderIdText.text = "Order ID: $orderId"
        binding.orderStatusText.text = "Status: $orderStatus"
        binding.trackingDetailsText.text = trackingDetails

        // Show rating option only for delivered orders
        if (orderStatus == "Delivered") {
            binding.ratingButton.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    showRatingDialog()
                }
            }
        } else {
            binding.ratingButton.visibility = View.GONE
        }
    }

    private fun showRatingDialog() {
        val dialog = RatingDialogFragment { rating, review ->
            // Handle the rating submission (e.g., save to database)
            // Here, you can handle the rating submission
            // For now, we'll just print the rating and review
            println("Rating: $rating, Review: $review")
        }
        dialog.show(supportFragmentManager, "RatingDialogFragment")
    }
}
