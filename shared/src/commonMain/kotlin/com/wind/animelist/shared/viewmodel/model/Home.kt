package com.wind.animelist.shared.viewmodel.model

import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga

/**
 * Created by Phong Huynh on 9/29/2020
 */

interface HomeItem {
    fun getType(): Int
}

data class HomeManga(
    val list: List<Manga>
): HomeItem {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_MANGA_LIST
    }
}

data class HomeAnime(
    val list: List<Anime>
): HomeItem {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_ANIME_LIST
    }
}

data class Title(val text: String): HomeItem, DetailManga {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_TITLE
    }
}

object Divider: HomeItem {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_DIVIDER
    }
}