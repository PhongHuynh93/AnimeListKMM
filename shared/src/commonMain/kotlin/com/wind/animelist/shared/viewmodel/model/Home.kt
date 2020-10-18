package com.wind.animelist.shared.viewmodel.model

import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga

/**
 * Created by Phong Huynh on 9/29/2020
 */

interface Home {
    fun getType(): Int
}

data class MangaList(
    val list: List<Manga>,
    val title: String
): Home {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_MANGA_LIST
    }
}

data class AnimeList(
    val list: List<Anime>
): Home {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_ANIME_LIST
    }
}