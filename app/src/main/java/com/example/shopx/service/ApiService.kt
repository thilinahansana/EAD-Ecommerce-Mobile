package com.example.shopx.service

import com.example.shopx.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("Products")
    suspend fun getProducts(): List<Product>
}

object RetrofitInstance {
    private const val BASE_URL = "http://[2402:d000:a500:f62:11f7:6b65:8ec4:58a8]/api/v1/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}