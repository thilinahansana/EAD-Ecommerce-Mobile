package com.example.shopx.service

import com.example.shopx.model.OrderRequest
import com.example.shopx.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("vendor/products/all")
    suspend fun getProducts(): List<Product>

    // New method to get a product by ID
    @GET("vendor/products/product/{productId}")
    suspend fun getProductById(@Path("productId") productId: String): Product


    @POST("Order/")
    fun postOrder(@Body orderData: OrderRequest): Call<Void>
}
