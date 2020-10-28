package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.AnimeDataMapper
import com.wind.animelist.shared.domain.model.Anime
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 9/28/2020
 */
data class GetTopAnimeParam(val subType: String)
class GetTopAnimeUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<GetTopAnimeParam, List<Anime>>(dispatcher) {

    override suspend fun execute(parameters: GetTopAnimeParam): List<Anime> {
        return repository.getTopAnime(parameters.subType).data
            .map {
                AnimeDataMapper().map(it)
            }.filter {
                it.isValid()
            }
    }
}