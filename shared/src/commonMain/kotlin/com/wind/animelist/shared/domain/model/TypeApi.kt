package com.wind.animelist.shared.domain.model

/**
 * Created by Phong Huynh on 10/30/2020
 */
interface TypeAPI {
    fun getType(): String
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
}