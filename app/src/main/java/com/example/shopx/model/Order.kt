package com.example.shopx.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val customerId: String,
    val orderId: String,
    val status: String?,
    val note: String?,
    val canceledBy: String?,
    val createdAt: String,
    val items: List<OrderItem>
) : Parcelable

data class OrderRequest(
    val customerId: String?,
    val vendorId: String?,
    val productId: String?,
    val productName: String?,
    val quantity: Int,
    val price: String,
    val imageUrl: String?,
    val size: String = "Default"
)



@Parcelize
data class OrderItem(
    val itemId: String,
    val orderId: String,
    val productId: String,
    val vendorId: String,
    val productName: String,
    val quantity: Int,
    val price: String,
    val size: String,
    val imageUrl: String,
    val isCanceled: Boolean,
    val isActive: Boolean,
    val status: String,
    val createdAt: String
) : Parcelable

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


data class CartUpdateRequest(
    val quantity: Int,
    val size: String? = null
)

data class OrderResponse(
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

data class OrderHistoryResponse(
    val orderId: String,
    val status: String,
    val items: List<OrderItem>,
    val createdAt: String
)

data class OrderDetails(val address: String, val phone: String)