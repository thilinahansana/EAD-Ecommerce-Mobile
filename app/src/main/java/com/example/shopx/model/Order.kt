package com.example.shopx.model

data class Order(
    val orderId: String,
    val orderDate: String,
    val orderStatus: String,
    val trackingDetails: String
)

data class OrderRequest(
    val customerId: String?,
    val vendorId: String? = null,
    val productId: String?,
    val productName: String?,
    val quantity: Int,
    val price: String,
    val imageUrl: String?,
    val size: String = "Default"
)

data class OrderData(
    val customerId: String,
    val status: String? = null,
    val note: String? = null,
    val canceledBy: String? = null,
    val productId: String,
    val quantity: Int,
    val price: String,
    val productName: String,
    val vendorId: String? = null,
    val createdAt: String,
    val dispatchedAt: String? = null,
    val deliveredAt: String? = null,
    val items: List<OrderItem>
)

data class OrderItem(
    val itemId: String,
    val productId: String,
    val vendorId: String
)