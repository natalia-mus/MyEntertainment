package com.example.myentertainment.data

data class ProblemReport(
    val id: String?,
    val user: String?,
    val deviceModel: String?,
    val deviceManufacturer: String?,
    val androidVersion: String?,
    val summary: String?,
    val description: String?,
    val date: Date?
)