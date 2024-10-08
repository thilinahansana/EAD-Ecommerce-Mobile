package com.example.shopx.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Address(
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val zipCode: String
)

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: Int,
    val profilePicture: String?,
    val phoneNumber: String?,
    val addresss: Address?,
    val updatedDate: String
)

@JsonClass(generateAdapter = true)
data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val re_Password: String,
    val phoneNumber: String,
    val addresss: Address,
    val role: Int = 2,
    val createdDate: String,
    val updatedDate: String,
    val isActive: Boolean = true,
    val profilePicture: String?
)

data class PersonalInfo(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val rePassword: String,
    val phoneNumber: String,
    val profileImageUrl: String? = null
)

data class UpdateUserRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val role: Int?,
    val address: Address,
    val profilePicture: String?,
    val updatedDate: String,
)


