package com.example.shopx.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shopx.R
import com.example.shopx.databinding.ItemLayoutBinding
import com.example.shopx.model.FeedbackRequest
import com.example.shopx.model.OrderItem
import com.example.shopx.service.ApiService
import com.example.shopx.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderItemAdapter(
    private val context: Context,
    private val items: List<OrderItem>,
    private val apiService: ApiService,
    private val sessionManager: SessionManager
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
                showRatingDialog(item)
            }

        }

        private fun showRatingDialog(item: OrderItem) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_rating, null)
            val ratingBar = dialogView.findViewById<RatingBar>(R.id.rating_bar)
            val feedbackMessage = dialogView.findViewById<EditText>(R.id.feedback_message)

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Rate Item")
                .setPositiveButton("Submit") { _, _ ->
                    val rating = ratingBar.rating.toInt()
                    val message = feedbackMessage.text.toString()

                    if (rating > 0 && message.isNotEmpty()) {
                        submitFeedback(item, rating, message)
                    } else {
                        Toast.makeText(context, "Please provide a rating and feedback", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }

        private fun submitFeedback(item: OrderItem, rating: Int, feedbackMessage: String) {

            val currentDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
                Date()
            )
            val feedbackRequest = sessionManager.getUser()?.id?.let {
                FeedbackRequest(
                    customerId = it, // Replace with actual customerId
                    orderId = item.orderId,
                    productId = item.productId,
                    feedbackMessage = feedbackMessage,
                    rating = rating,
                    date = currentDateTime
                )
            }

            if (feedbackRequest != null) {
                apiService.submitFeedback(feedbackRequest).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.i("String" , "Rating ${response.body()}")
                        Log.i("String" , "Rating ${response.message()}")
                        Log.i("String" , "Rating $feedbackRequest")
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to submit feedback", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}
