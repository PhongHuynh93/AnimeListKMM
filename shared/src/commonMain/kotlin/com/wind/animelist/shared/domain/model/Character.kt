package com.wind.animelist.shared.domain.model

import com.wind.animelist.shared.util.Parcelable
import com.wind.animelist.shared.util.Parcelize

/**
 * Created by Phong Huynh on 10/10/2020
 */
data class Character (override val id: Int, override val imageUrl: String, override val title: String, val role: String): BaseModel(id, imageUrl, title) {

}