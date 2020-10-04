package com.wind.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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