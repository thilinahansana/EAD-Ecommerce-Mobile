package com.example.shopx

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shopx.databinding.ActivityProductDetailsBinding
import com.example.shopx.model.OrderRequest
import com.example.shopx.service.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopx.adapter.FeedbackAdapter
import com.example.shopx.model.FeedbackResponse
import com.example.shopx.model.FeedbackUpdateRequest
import com.example.shopx.model.VendorRatingResponse
import com.example.shopx.session.SessionManager

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Initialize Retrofit
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5001/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Get product data from the intent
        val productId = intent.getStringExtra("productId")
        val vendorId = intent.getStringExtra("vendorId")
        val productName = intent.getStringExtra("productName")
        val productDescription = intent.getStringExtra("productDescription")
        val productPrice = intent.getDoubleExtra("productPrice", 0.0)
        val productImage = intent.getStringExtra("productImage")

        // Set the data to views
        binding.tvDetailsProductName.text = productName
        binding.tvDetailsProductDescription.text = productDescription
        binding.tvDetailsProductPrice.text = "$productPrice"

        // Load product image
        Glide.with(this)
            .load(productImage)
            .into(binding.ivDetails)

        // Get user from SessionManager
        val user = sessionManager.getUser()

        if (user == null) {
            Log.e("ProductDetailsActivity", "User data not found in session")
            Toast.makeText(this, "User session error. Please log in again.", Toast.LENGTH_LONG).show()
            // TODO: Redirect to login screen
            return
        }

        // Prepare order data for POST request
        val orderData = prepareOrderData(user.id, productId, productName, vendorId, productPrice, productImage)

        binding.btnDetailsAddToCart.setOnClickListener {
            postOrderData(orderData)
        }

        // Fetch vendor rating
        fetchVendorRating(vendorId)

        // Set up feedback count button
        binding.btnFeedbackCount.setOnClickListener {
            showFeedbackBottomSheet(vendorId)
        }
    }





    private fun prepareOrderData(customerId: String?, productId: String?, productName: String?, vendorId: String?, productPrice: Double, productImage: String?): OrderRequest {
        return OrderRequest(
            customerId = customerId,
            productId = productId,
            productName = productName,
            vendorId = vendorId,
            quantity = 1,
            price = productPrice.toString(),
            imageUrl = productImage,
        )
    }

    private fun postOrderData(orderData: OrderRequest) {
        binding.btnDetailsAddToCart.isEnabled = false // Disable button to prevent multiple clicks

        apiService.postOrder(orderData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                binding.btnDetailsAddToCart.isEnabled = true // Re-enable button
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductDetailsActivity, "Added to cart successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProductDetailsActivity, "Failed to add to cart. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                binding.btnDetailsAddToCart.isEnabled = true // Re-enable button
                Toast.makeText(this@ProductDetailsActivity, "Network error. Please check your connection and try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchVendorRating(vendorId: String?) {
        Log.i("String" , "Vender id  $vendorId")
        if (vendorId == null) return



        apiService.getVendorRating(vendorId).enqueue(object : Callback<VendorRatingResponse> {
            override fun onResponse(call: Call<VendorRatingResponse>, response: Response<VendorRatingResponse>) {
                if (response.isSuccessful) {
                    val vendorRating = response.body()
                    if (vendorRating != null) {
                        updateVendorRatingUI(vendorRating)
                    } else {
                        Log.e("ProductDetailsActivity", "Vendor rating response body is null")
                        showErrorMessage("Unable to load vendor rating")
                    }
                } else {
                    Log.e("ProductDetailsActivity", "Failed to fetch vendor rating: ${response.code()}")
                    showErrorMessage("Failed to load vendor rating")
                }
            }

            override fun onFailure(call: Call<VendorRatingResponse>, t: Throwable) {
                Log.e("ProductDetailsActivity", "Error fetching vendor rating: ${t.message}", t)
                showErrorMessage("Network error. Please try again.")
            }
        })
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateVendorRatingUI(vendorRating: VendorRatingResponse) {
        val feedbackInfo = vendorRating.user?.result?.feedbackInfo
        val feedbackCount = feedbackInfo?.feedbackCount ?: 0
        val sumOfRating = feedbackInfo?.sumOfRating ?: 0
        val averageRating = if (feedbackCount > 0) sumOfRating.toFloat() / feedbackCount else 0f

        binding.rbVendorRating.rating = averageRating
        binding.tvVendorRatingValue.text = String.format("%.1f", averageRating)
        binding.btnFeedbackCount.text = "$feedbackCount feedbacks"
    }

    private fun showFeedbackBottomSheet(vendorId: String?) {
        if (vendorId == null) return

        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_feedback, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val recyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.rvFeedback)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val currentUserId = sessionManager.getUser()?.id ?: ""

        // Fetch feedback list
        apiService.getVendorFeedback(vendorId).enqueue(object : Callback<List<FeedbackResponse>> {
            override fun onResponse(call: Call<List<FeedbackResponse>>, response: Response<List<FeedbackResponse>>) {
                if (response.isSuccessful) {
                    val feedbackList = response.body()
                    feedbackList?.let {
                        val adapter = FeedbackAdapter(it, currentUserId) { feedback ->
                            showEditFeedbackDialog(feedback)
                        }
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("ProductDetailsActivity", "Failed to fetch feedback: ${response.code()}")
                    Toast.makeText(this@ProductDetailsActivity, "Failed to load feedback", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<FeedbackResponse>>, t: Throwable) {
                Log.e("ProductDetailsActivity", "Error fetching feedback: ${t.message}", t)
                Toast.makeText(this@ProductDetailsActivity, "Network error. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
        bottomSheetDialog.show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showEditFeedbackDialog(feedback: FeedbackResponse) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val ratingBar = dialogView.findViewById<RatingBar>(R.id.rating_bar)
        val feedbackMessage = dialogView.findViewById<EditText>(R.id.feedback_message)
        val submitButton = dialogView.findViewById<Button>(R.id.submit_feedback_button)

        ratingBar.rating = feedback.rating.toFloat()
        feedbackMessage.setText(feedback.comment)

        submitButton.setOnClickListener {
            val updatedRating = ratingBar.rating.toInt()
            val updatedMessage = feedbackMessage.text.toString()

            val updateRequest = FeedbackUpdateRequest(
                customerId = feedback.customerId,
                orderId = "order",
                productId = "product",
                feedbackMessage = updatedMessage,
                rating = updatedRating,
                date = feedback.createdDate
            )

            updateFeedback(feedback.id, updateRequest)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun updateFeedback(feedbackId: String, updateRequest: FeedbackUpdateRequest) {
        apiService.updateFeedback(feedbackId, updateRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductDetailsActivity, "Feedback updated successfully", Toast.LENGTH_SHORT).show()
                    // Refresh the feedback list
                    showFeedbackBottomSheet(intent.getStringExtra("vendorId"))
                } else {
                    Log.e("ProductDetailsActivity", "Failed to update feedback: ${response.code()}")
                    Toast.makeText(this@ProductDetailsActivity, "Failed to update feedback", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ProductDetailsActivity", "Error updating feedback: ${t.message}", t)
                Toast.makeText(this@ProductDetailsActivity, "Network error. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
