package com.example.shopx

import android.os.Bundle
import android.util.Log
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

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://your.api.base.url/") // Change this to your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Get product data from the intent
        val productId = intent.getStringExtra("productId")
        val productName = intent.getStringExtra("productName")
        val productDescription = intent.getStringExtra("productDescription")
        val productPrice = intent.getDoubleExtra("productPrice", 0.0)
        val productImage = intent.getStringExtra("productImage")
        val productStockQuantity = intent.getIntExtra("productStockQuantity", 0)

        // Get customerId from the JWT token
        val customerId = getCustomerIdFromSession()

        // Set the data to views
        binding.tvDetailsProductName.text = productName
        binding.tvDetailsProductDescription.text = productDescription
        binding.tvDetailsProductPrice.text = "$productPrice"
        // Optional: Display stock quantity if needed
        // binding.tvStockQuantity.text = "In stock: $productStockQuantity"

        // Load product image
        Glide.with(this)
            .load(productImage)
            .into(binding.ivDetails)

        // Prepare order data for POST request
        val orderData = prepareOrderData(customerId, productId, productName, productPrice, productImage)

        binding.btnDetailsAddToCart.setOnClickListener{
            postOrderData(orderData)
        }
        // Make the API call to post order data

    }

    private fun getCustomerIdFromSession(): String? {
        val token = sessionManager.getToken() ?: return null
        val claims = sessionManager.decodeToken() ?: return null
        return claims["customerId"] as? String // Adjust based on your JWT structure
    }

    private fun prepareOrderData(customerId: String?, productId: String?, productName: String?, productPrice: Double, productImage: String?): OrderRequest {
        return OrderRequest(
            customerId = customerId,
            productId = productId,
            productName = productName,
            quantity = 1, // Adjust as necessary
            price = productPrice.toString(),
            imageUrl = productImage
        )
    }

    private fun postOrderData(orderData: OrderRequest) {
        apiService.postOrder(orderData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("ProductDetailsActivity", "Order successfully posted.")
                } else {
                    Log.e("ProductDetailsActivity", "Failed to post order: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ProductDetailsActivity", "Error posting order: ${t.message}", t)
            }
        })
    }
}
