package com.wind.animelist.shared.domain.model

import com.wind.animelist.shared.util.Parcelable
import com.wind.animelist.shared.util.Parcelize

/**
 * Created by Phong Huynh on 10/3/2020
 */
@Parcelize
data class Anime(
    val id: Int,
    val imageUrl: String,
    val title: String,
): Parcelable {
    fun isValid() = id > 0
}