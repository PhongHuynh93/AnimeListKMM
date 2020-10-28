package com.wind.animelist.shared.domain.model

import com.wind.animelist.shared.util.Parcelable
import com.wind.animelist.shared.util.Parcelize

/**
 * Created by Phong Huynh on 10/3/2020
 */
data class Manga (override val id: Int, override val imageUrl: String, override val title: String): BaseModel(id, imageUrl, title) {

}