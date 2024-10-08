package com.example.shopx.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Order Model
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
    val rating: Int = 0,
    val status: String,
    val createdAt: String
) : Parcelable



data class OrderDetails(val address: String, val phone: String)
