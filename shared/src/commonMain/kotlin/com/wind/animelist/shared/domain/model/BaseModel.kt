package com.wind.animelist.shared.domain.model

import com.wind.animelist.shared.util.Parcelable
import com.wind.animelist.shared.util.Parcelize

/**
 * Created by Phong Huynh on 10/3/2020
 */
@Parcelize
open class BaseModel(
    open val id: Int,
    open val imageUrl: String,
    open val title: String,
): Parcelable {
    fun isValid() = id > 0
}