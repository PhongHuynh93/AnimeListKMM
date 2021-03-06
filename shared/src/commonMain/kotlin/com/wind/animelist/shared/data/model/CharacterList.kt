package com.wind.animelist.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterList<T> (
    @SerialName("request_cache_expiry")
    val requestCacheExpiry: Int,
    @SerialName("request_cached")
    val requestCached: Boolean,
    @SerialName("request_hash")
    val requestHash: String,
    @SerialName("characters")
    val data: List<T> = emptyList()
)

