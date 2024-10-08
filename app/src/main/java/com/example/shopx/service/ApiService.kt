package com.example.shopx.service

import com.example.shopx.model.CartResponse
import com.example.shopx.model.FeedbackRequest
import com.example.shopx.model.FeedbackResponse
import com.example.shopx.model.FeedbackUpdateRequest
import com.example.shopx.model.NotificationResponse
import com.example.shopx.model.Order
import com.example.shopx.model.OrderDetails
import com.example.shopx.model.OrderRequest
import com.example.shopx.model.Product
import com.example.shopx.model.UpdateUserRequest
import com.example.shopx.model.VendorRatingResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @POST("Feedback/")
    fun submitFeedback(
        @Body feedbackRequest: FeedbackRequest
    ): Call<Void>

    @GET("Feedback/vender-ranking/{vendorId}")
    fun getVendorRating(@Path("vendorId") vendorId: String): Call<VendorRatingResponse>

    @GET("/api/v1/Feedback/product/feedback/{productId}")
    fun getVendorFeedback(@Path("productId") vendorId: String): Call<List<FeedbackResponse>>

    @PATCH("api/v1/Feedback")
    fun updateFeedback(@Query("feedbackId") feedbackId: String, @Body updateRequest: FeedbackUpdateRequest): Call<Void>

    @PATCH("update-user/{userID}")
    fun updateUser(
        @Path("userID") userID: String,
        @Body updateRequest: UpdateUserRequest
    ): Call<Void>

    @DELETE("/api/v1/deactivate-user/{customerID}")
    fun deactivateUser(@Path("customerID") customerID: String): Call<Void>



    //    @GET("vendor/products/product/{productId}")
    // suspend fun getProductById(@Path("productId") productId: String): Product


    // @POST("Order/")
    // fun postOrder(@Body orderData: OrderRequest): Call<Void>


    // @GET("Order/cart/")
    // fun getCartItems(@Query("customerId") customerId: String): Call<CartResponse>

    // @GET("Notification/my/notifications")
    // fun getNotifications(@Query("userId") userId: String): Call<List<NotificationResponse>>

    // @DELETE("Order/cart/item")
    // fun deleteCartItem(
    //     @Query("orderId") orderId: String,
    //     @Query("itemId") itemId: String
    // ): Call<Void>

    // @POST("Order/placing")
    // fun placeOrder(
    //     @Query("orderId") orderId: String,
    //     @Body orderRequest: OrderDetails
    // ): Call<Void>


    // @GET("Order/history")
    // fun getOrderHistory(@Query("customerId") userId: String): Call<List<Order>>


    // @POST("Order/request/cancel")
    // fun cancelOrder(@Body cancelRequest: Map<String, String>): Call<Void>

    // @POST("Feedback/")
    // fun submitFeedback(
    //     @Body feedbackRequest: FeedbackRequest
    // ): Call<Void>

    // @GET("Feedback/vender-ranking/{vendorId}")
    // fun getVendorRating(@Path("vendorId") vendorId: String): Call<VendorRatingResponse>

    // @GET("/api/v1/Feedback/product/feedback/{productId}")
    // fun getVendorFeedback(@Path("productId") vendorId: String): Call<List<FeedbackResponse>>

    // @PATCH("api/v1/Feedback")
    // fun updateFeedback(@Query("feedbackId") feedbackId: String, @Body updateRequest: FeedbackUpdateRequest): Call<Void>

    // @PATCH("update-user/{userID}")
    // fun updateUser(
    //     @Path("userID") userID: String,
    //     @Body updateRequest: UpdateUserRequest
    // ): Call<Void>

    // @DELETE("/api/v1/deactivate-user/{customerID}")
    // fun deactivateUser(@Path("customerID") customerID: String): Call<Void>





























































}

