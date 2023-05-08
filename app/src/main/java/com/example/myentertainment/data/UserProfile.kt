package com.example.myentertainment.data

data class UserProfile(
    val userId: String? = null,
    val username: String? = null,
    val realName: String? = null,
    val city: String? = null,
    val country: String? = null,
    val birthDate: Date? = null,
    val email: String? = null
)