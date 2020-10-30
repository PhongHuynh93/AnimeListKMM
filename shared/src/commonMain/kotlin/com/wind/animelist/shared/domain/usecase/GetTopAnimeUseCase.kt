package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.AnimeDataMapper
import com.wind.animelist.shared.domain.model.Anime
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 9/28/2020
 */
data class GetTopAnimeParam(val subType: String, val page: Int, val isRefreshing: Boolean)
class GetTopAnimeUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<GetTopAnimeParam, List<Anime>>(dispatcher) {
    private var list = mutableListOf<Anime>()

    override suspend fun execute(parameters: GetTopAnimeParam): List<Anime> {
        return repository.getTopAnime(parameters.subType, parameters.page).data
            .map {
                AnimeDataMapper().map(it)
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