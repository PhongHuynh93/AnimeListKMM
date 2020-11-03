package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.MangaDataMapper
import com.wind.animelist.shared.domain.model.Manga
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 9/26/2020
 */
data class GetTopMangaParam(val subType: String, val loadedPage: Int = 1, val isRefreshing: Boolean)
class GetTopMangaUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<GetTopMangaParam, List<Manga>>(dispatcher) {
    private var list = mutableListOf<Manga>()
    private var page = 1

    override suspend fun execute(parameters: GetTopMangaParam): List<Manga> {
        if (parameters.isRefreshing) {
            list.clear()
            page = parameters.loadedPage
        }
        return repository.getTopManga(parameters.subType, page).data
            .map {
                MangaDataMapper().map(it)
            }.filter {
                it.isValid()
            }.distinct()
            .filter {
                var duplicated = false
                for (item in list) {
                    if (item.id == it.id) {
                        duplicated = true
                        break
                    }
                }
                !duplicated
            }.also {
                list.addAll(it)
                page++
            }
    }
}
