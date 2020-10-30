package com.wind.animelist.shared.viewmodel.model

import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.LoadMoreInfo
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.util.Parcelable
import com.wind.animelist.shared.util.Parcelize

/**
 * Created by Phong Huynh on 9/29/2020
 */

interface Home {
    fun getType(): Int
}

@Parcelize
data class MangaList(
    val title: String,
    val list: List<Manga>,
    val loadMoreInfo: LoadMoreInfo,
    val page: Int
): Home, Parcelable {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_MANGA_LIST
    }
}

data class AnimeList(
    val list: List<Anime>,
    val title: String
): Home {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_ANIME_LIST
    }
}