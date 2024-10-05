package com.example.shopx.model

import java.time.ZonedDateTime

data class CartResponse(
    val orderId: String,
    val customerId: String,
    val status: String?,
    val address: String?,
    val isInCart: Boolean,
    val note: String?,
    val tel: String?,
    val canceledBy: String?,
    val createdAt: String,
    val deliveredAt: String?,
    val items: List<CartItem>
)

data class CartItem(
    val itemId: String,
    val productId: String?,
    val vendorId: String?,
    val size: String
)


data class CartItemWithDetails(
    val cartItem: CartItem,
    val product: Product?, // Assuming you have a Product model
    var quantity: Int = 1 // Default quantity is set to 1
)
