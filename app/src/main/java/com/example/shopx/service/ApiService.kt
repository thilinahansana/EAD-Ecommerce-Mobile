package com.example.shopx.service

import com.example.shopx.model.CartResponse
import com.example.shopx.model.NotificationResponse
import com.example.shopx.model.Order
import com.example.shopx.model.OrderDetails
import com.example.shopx.model.OrderHistoryResponse
import com.example.shopx.model.OrderRequest
import com.example.shopx.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @GET("Notification/my/notifications")
    fun getNotifications(@Query("userId") userId: String): Call<List<NotificationResponse>>

    @DELETE("Order/cart/item")
    fun deleteCartItem(
        @Query("orderId") orderId: String,
        @Query("itemId") itemId: String
    ): Call<Void>

    @POST("Order/placing")
    fun placeOrder(
        @Query("orderId") orderId: String,
        @Body orderRequest: OrderDetails
    ): Call<Void>


    @GET("Order/history")
    fun getOrderHistory(@Query("customerId") userId: String): Call<List<Order>>


    @POST("Order/request/cancel")
    fun cancelOrder(@Body cancelRequest: Map<String, String>): Call<Void>

//    @PUT("Order/cart/{itemId}")
//    suspend fun updateCartItem(
//        @Path("itemId") itemId: String,
//        @Body updateRequest: CartUpdateRequest
//    ): Response<Void>
//
//    @DELETE("Order/cart/{itemId}")
//    suspend fun removeCartItem(@Path("itemId") itemId: String): Response<Void>
}

