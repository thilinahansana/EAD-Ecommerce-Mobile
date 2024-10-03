package com.example.shopx.service

import com.example.shopx.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("products/all")
    suspend fun getProducts(): List<Product>
}

object RetrofitInstance {
    private const val BASE_URL = "http://[2402:d000:a500:1bf1:a453:c841:56d8:19b1]/api/v1/vendor/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}