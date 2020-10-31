package com.wind.animelist.shared.domain.model

/**
 * Created by Phong Huynh on 10/30/2020
 */
interface TypeAPI {
    fun getType(): String
    // manga
    object TopManga: TypeAPI {
        override fun getType() = "manga"
    }
    object TopNovels: TypeAPI {
        override fun getType() = "novels"
    }
    object TopOneShots: TypeAPI {
        override fun getType() = "oneshots"
    }
    object TopManhwa: TypeAPI {
        override fun getType() = "manhwa"
    }
    object TopManhu: TypeAPI {
        override fun getType() = "manhua"
    }
    // anime
    object TopAiring: TypeAPI {
        override fun getType() = "airing"
    }
    object TopUpcoming: TypeAPI {
        override fun getType() = "upcoming"
    }
    object TopTv: TypeAPI {
        override fun getType() = "tv"
    }
    object TopMovie: TypeAPI {
        override fun getType() = "movie"
    }
    object TopOva: TypeAPI {
        override fun getType() = "ova"
    }
    object TopSpecial: TypeAPI {
        override fun getType() = "special"
    }
}