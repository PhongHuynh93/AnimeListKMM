package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.MangaDataMapper
import com.wind.animelist.shared.domain.model.LoadMoreInfo
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.domain.model.Response
import com.wind.animelist.shared.domain.model.TypeAPI
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 11/1/2020
 */
data class GetMangaHomeParam(val isRefreshing: Boolean)
class GetMangaHomeUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<GetMangaHomeParam, Response<List<Manga>>>(dispatcher) {
    private var page = 0
    override suspend fun execute(parameters: GetMangaHomeParam): Response<List<Manga>> {
        if (parameters.isRefreshing) {
            page = 0
        }
        val apiType = when (page) {
            0 -> {
                TypeAPI.TopManga
            }
            1 -> {
                TypeAPI.TopNovels
            }
            2 -> {
                TypeAPI.TopOneShots
            }
            3 -> {
                TypeAPI.TopManhwa
            }
            4 -> {
                TypeAPI.TopManhu
            }
            else -> {
                throw IllegalStateException("must be one of subType")
            }
        }
        return repository.getTopManga(apiType.getType(), 1).data
            .map {
                MangaDataMapper().map(it)
            }.filter {
                it.isValid()
            }.distinct()
            .let {
                val isMore = page < 4
                page++
                Response(it, LoadMoreInfo(apiType), 1, isMore)
            }
    }
}