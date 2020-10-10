package com.wind.animelist.shared.domain.model

import com.wind.animelist.shared.util.Parcelable
import com.wind.animelist.shared.util.Parcelize

/**
 * Created by Phong Huynh on 10/10/2020
 */
@Parcelize
data class Character(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val role: String,
): Parcelable {
    fun isValid() = id > 0
}