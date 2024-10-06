package com.example.shopx.model

data class NotificationResponse(
    val notifyId: String,
    val message: String,
    val userId: String?,
    val reason: String,
    val isRead: Boolean,
    val sentDate: String
)
