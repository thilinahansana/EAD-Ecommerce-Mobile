package com.example.shopx.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//Product Model
@Parcelize
data class Product(
    val productId: String,
    val name: String,
    val description: String,
    val price: Double,
    val vendorId: String,
    val imageUrl: String,
    val stockQuantity: Int,
    val type: String,
    val size: String,
    var isLiked: Boolean = false
) : Parcelable

