package com.wind.animelist.shared.domain.model


/**
 * Created by Phong Huynh on 10/3/2020
 */
data class Anime (override val id: Int, override val imageUrl: String, override val title: String): BaseModel(id, imageUrl, title) {

}