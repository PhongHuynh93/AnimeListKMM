package com.wind.animelist.shared.viewmodel.model

import com.wind.animelist.shared.domain.model.Character

/**
 * Created by Phong Huynh on 10/10/2020
 */
interface DetailManga {
    fun getType(): Int
}

data class DetailMangaMoreInfo(val title: String, val description: String): DetailManga {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_MORE_INFO
    }
}

data class DetailMangaCharacter(val title: String, val list: List<Character>): DetailManga {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_CHARACTER_LIST
    }
}
