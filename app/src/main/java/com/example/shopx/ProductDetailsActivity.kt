package com.example.shopx

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Initialize Retrofit with longer timeout
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
        val orderData = prepareOrderData(user.id, productId, productName,vendorId, productPrice, productImage)

        binding.btnDetailsAddToCart.setOnClickListener {
            postOrderData(orderData)
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

        Log.i("String" , "Order Details  $orderData")

        apiService.postOrder(orderData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                binding.btnDetailsAddToCart.isEnabled = true // Re-enable button
                if (response.isSuccessful) {
                    Log.d("ProductDetailsActivity", "Order successfully posted.")
                    Toast.makeText(this@ProductDetailsActivity, "Added to cart successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ProductDetailsActivity", "Failed to post order: ${response.code()}")
                    Toast.makeText(this@ProductDetailsActivity, "Failed to add to cart. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                binding.btnDetailsAddToCart.isEnabled = true // Re-enable button
                Log.e("ProductDetailsActivity", "Error posting order: ${t.message}", t)
                Toast.makeText(this@ProductDetailsActivity, "Network error. Please check your connection and try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}