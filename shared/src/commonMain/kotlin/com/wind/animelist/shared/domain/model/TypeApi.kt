package com.wind.animelist.shared.domain.model

import com.wind.animelist.shared.util.Parcelable
import com.wind.animelist.shared.util.Parcelize

/**
 * Created by Phong Huynh on 10/30/2020
 */
interface TypeAPI: Parcelable {
    fun getType(): String
    // manga
    @Parcelize
    object TopManga: TypeAPI {
        override fun getType() = "manga"
    }
    @Parcelize
    object TopNovels: TypeAPI {
        override fun getType() = "novels"
    }
    @Parcelize
    object TopOneShots: TypeAPI {
        override fun getType() = "oneshots"
    }
    @Parcelize
    object TopManhwa: TypeAPI {
        override fun getType() = "manhwa"
    }
    @Parcelize
    object TopManhu: TypeAPI {
        override fun getType() = "manhua"
    }

    ///////////////////////////////////////////////// anime
    @Parcelize
    object TopAiring: TypeAPI {
        override fun getType() = "airing"
    }
    @Parcelize
    object TopUpcoming: TypeAPI {
        override fun getType() = "upcoming"
    }
    @Parcelize
    object TopTv: TypeAPI {
        override fun getType() = "tv"
    }
    @Parcelize
    object TopMovie: TypeAPI {
        override fun getType() = "movie"
    }
    @Parcelize
    object TopOva: TypeAPI {
        override fun getType() = "ova"
    }
    @Parcelize
    object TopSpecial: TypeAPI {
        override fun getType() = "special"
    }
}