package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.MangaDataMapper
import com.wind.animelist.shared.domain.model.Manga
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 9/26/2020
 */
data class GetTopMangaParam(val subType: String, val page: Int, val isRefreshing: Boolean)
class GetTopMangaUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<GetTopMangaParam, List<Manga>>(dispatcher) {
    private var list = mutableListOf<Manga>()

    override suspend fun execute(parameters: GetTopMangaParam): List<Manga> {
        if (parameters.isRefreshing) {
            list.clear()
        }
        return repository.getTopManga(parameters.subType, parameters.page).data
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
            }
    }
}
