package com.wind.animelist.shared.viewmodel.model

import com.wind.animelist.shared.domain.model.Manga

/**
 * Created by Phong Huynh on 10/8/2020
 */
interface DetailManga {
    fun getType(): Int
    override fun equals(other: Any?): Boolean
}

data class DetailMangaHeader (
    val manga: Manga
): DetailManga {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_DETAIL_MANGA_HEADER
    }
}
