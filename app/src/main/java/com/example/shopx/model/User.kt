package com.example.shopx.model

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: String
)

data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val re_Password: String,
    val phoneNumber: String,
    val address: Address,
    val role: Int = 1,
    val createdDate: String,
    val updatedDate: String,
    val isActive: Boolean = true,
    val profilePicture: String = ""
)

