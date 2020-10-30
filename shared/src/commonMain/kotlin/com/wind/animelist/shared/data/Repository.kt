package com.wind.animelist.shared.data

import com.wind.animelist.shared.data.model.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Created by Phong Huynh on 9/24/2020
 */
interface Repository {
    suspend fun getTopAnime(subType: String): TopList<NetworkAnime>
    suspend fun getTopManga(subType: String, page: Int): TopList<NetworkManga>
    suspend fun getMangaCharacter(id: Int): CharacterList<NetworkCharacter>
    suspend fun getMoreInfo(id: Int): NetworkMoreInfo
}

private const val endpoint = "https://api.jikan.moe"
internal class RepositoryImpl internal constructor(private val client: HttpClient) : Repository {
    private fun HttpRequestBuilder.apiUrl(path: String) {
        url {
            takeFrom(endpoint)
            encodedPath = path
        }
    }

    override suspend fun getTopAnime(subType: String): TopList<NetworkAnime> {
        return client.get {
            apiUrl("v3/top/anime/1/$subType")
        }
    }

    override suspend fun getTopManga(subType: String, page: Int): TopList<NetworkManga> {
        return client.get {
            apiUrl("v3/top/manga/$page/$subType")
        }
    }

    override suspend fun getMangaCharacter(id: Int): CharacterList<NetworkCharacter> {
        return client.get {
            apiUrl("/v3/manga/$id/characters")
        }
    }

    override suspend fun getMoreInfo(id: Int): NetworkMoreInfo {
        return client.get {
            apiUrl("/v3/manga/$id/moreinfo")
        }
    }
}