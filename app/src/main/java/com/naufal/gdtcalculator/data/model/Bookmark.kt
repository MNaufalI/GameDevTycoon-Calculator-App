package com.naufal.gdtcalculator.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Bookmark(
    val id: String,
    val topic: String,
    val primaryGenre: String,
    val secondaryGenre: String = "",
    val audience: String = "",
    val savedAt: Long = System.currentTimeMillis()
)