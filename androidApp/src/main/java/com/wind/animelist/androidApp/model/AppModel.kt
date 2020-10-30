package com.wind.animelist.androidApp.model

import android.os.Parcelable
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.viewmodel.model.AdapterTypeUtil
import com.wind.animelist.shared.viewmodel.model.DetailManga
import com.wind.animelist.shared.viewmodel.model.Home
import com.wind.animelist.shared.viewmodel.model.MangaList
import kotlinx.android.parcel.Parcelize

/**
 * Created by Phong Huynh on 10/18/2020
 */
@Parcelize
data class TitleManga(val mangaList: MangaList): Home, DetailManga, Parcelable {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_TITLE
    }
}

@Parcelize
data class Title(val text: String): Home, DetailManga, Parcelable {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_TITLE
    }
}

// TODO: 10/30/2020 need to refactor to support loadmore
@Parcelize
data class TitleAnime(val text: String, val list: List<Anime>): Home, DetailManga, Parcelable {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_TITLE
    }
}

object Divider: Home {
    override fun getType(): Int {
        return AdapterTypeUtil.TYPE_DIVIDER
    }
}