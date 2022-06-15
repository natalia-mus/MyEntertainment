package com.example.myentertainment.data

import com.example.myentertainment.Date

data class UserProfile(
    val username: String? = null,
    val realName: String? = null,
    val city: String? = null,
    val country: String? = null,
    val birthDate: Date? = null,
    val email: String? = null
)