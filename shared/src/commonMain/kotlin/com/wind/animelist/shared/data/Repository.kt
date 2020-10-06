package com.wind.animelist.shared.data

import com.wind.animelist.shared.data.model.NetworkAnime
import com.wind.animelist.shared.data.model.NetworkManga
import com.wind.animelist.shared.data.model.TopList
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Created by Phong Huynh on 9/24/2020
 */
interface Repository {
    suspend fun getTopAnime(): TopList<NetworkAnime>
    suspend fun getTopManga(subType: String): TopList<NetworkManga>
}

private const val endpoint = "https://api.jikan.moe"
internal class RepositoryImpl internal constructor(private val client: HttpClient) : Repository {
    private fun HttpRequestBuilder.apiUrl(path: String) {
        url {
            takeFrom(endpoint)
            encodedPath = path
        }
    }

    override suspend fun getTopAnime(): TopList<NetworkAnime> {
        return client.get {
            apiUrl("v3/top/anime/1/upcoming")
        }
    }

    override suspend fun getTopManga(subType: String): TopList<NetworkManga> {
        return client.get {
            apiUrl("v3/top/manga/1/$subType")
        }
    }
}