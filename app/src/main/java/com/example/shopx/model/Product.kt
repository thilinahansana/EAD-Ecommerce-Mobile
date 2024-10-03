package com.example.shopx.model

data class Product(
    val name: String,
    val description: String,
    val price: Double,
    val stockQuantity: Int,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val type: String, // For filtering by type (e.g., "Women", "Kids", "Men")
    val size: String,
    var isLiked: Boolean = false  // We'll keep this for client-side like functionality
)