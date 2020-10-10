package com.wind.animelist.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCharacter(
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("mal_id")
    val id: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("role")
    val role: String?,
    @SerialName("url")
    val url: String?
)
