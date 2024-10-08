package com.example.shopx.model

data class FeedbackRequest(
    val customerId: String,
    val orderId: String,
    val productId: String,
    val feedbackMessage: String,
    val rating: Int,
    val date: String
)

data class VendorRatingResponse(
    val user: UserRatingInfo?
)

data class UserRatingInfo(
    val result: UserRatingResult?
)

data class UserRatingResult(
    val feedbackInfo: FeedbackInfo?
)

data class FeedbackInfo(
    val feedbackCount: Int?,
    val sumOfRating: Int?
)

data class FeedbackResponse(
    val id: String,
    val customerId: String,
    val vendorId: String,
    val rating: Int,
    val comment: String,
    val createdDate: String
)

data class FeedbackUpdateRequest(
    val customerId: String,
    val orderId: String,
    val productId: String,
    val feedbackMessage: String,
    val rating: Int,
    val date: String
)