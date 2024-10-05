package com.example.shopx.service

import com.example.shopx.model.CartResponse
import com.example.shopx.model.OrderRequest
import com.example.shopx.model.OrderResponse
import com.example.shopx.model.Product
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("vendor/products/all")
    suspend fun getProducts(): List<Product>

    // New method to get a product by ID
    @GET("vendor/products/product/{productId}")
    suspend fun getProductById(@Path("productId") productId: String): Product


    @POST("Order/")
    fun postOrder(@Body orderData: OrderRequest): Call<Void>
//
//    @GET("Order/cart")
//    suspend fun getCartItems(@Query("customerId") customerId: String): Response<OrderResponse>

    @GET("Order/cart/")
    fun getCartItems(@Query("customerId") customerId: String): Call<CartResponse>



//    @PUT("Order/cart/{itemId}")
//    suspend fun updateCartItem(
//        @Path("itemId") itemId: String,
//        @Body updateRequest: CartUpdateRequest
//    ): Response<Void>
//
//    @DELETE("Order/cart/{itemId}")
//    suspend fun removeCartItem(@Path("itemId") itemId: String): Response<Void>
}
