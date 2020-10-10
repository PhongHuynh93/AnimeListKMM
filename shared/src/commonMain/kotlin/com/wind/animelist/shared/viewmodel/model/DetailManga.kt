package com.wind.animelist.shared.viewmodel.model

import com.wind.animelist.shared.domain.model.Character

/**
 * Created by Phong Huynh on 10/8/2020
 */
interface DetailManga {
    fun getType(): Int
    override fun equals(other: Any?): Boolean
}

data class MangaCharacter(
    val list: List<Character>
): DetailManga {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_CHARACTER_LIST
    }
}
