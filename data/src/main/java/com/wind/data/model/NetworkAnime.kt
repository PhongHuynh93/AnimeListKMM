package com.wind.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Phong Huynh on 9/27/2020
 */
@Serializable
data class NetworkAnime(
    @SerialName("end_date")
    val endDate: String?,
    @SerialName("episodes")
    val episodes: Int?,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("mal_id")
    val malId: Int?,
    @SerialName("members")
    val members: Int?,
    @SerialName("rank")
    val rank: Int?,
    @SerialName("score")
    val score: Int?,
    @SerialName("start_date")
    val startDate: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("type")
    val type: String?,
    @SerialName("url")
    val url: String?
)