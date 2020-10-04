package com.wind.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: 9/26/2020 create mapper to upper layer
@Serializable
data class TopList<T> (
    @SerialName("request_cache_expiry")
    val requestCacheExpiry: Int,
    @SerialName("request_cached")
    val requestCached: Boolean,
    @SerialName("request_hash")
    val requestHash: String,
    @SerialName("top")
    val data: List<T> = emptyList()
)

