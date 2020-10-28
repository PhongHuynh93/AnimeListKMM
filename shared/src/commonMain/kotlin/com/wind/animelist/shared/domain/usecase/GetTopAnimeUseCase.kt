package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.AnimeDataMapper
import com.wind.animelist.shared.domain.mapper.impl.MangaDataMapper
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 9/28/2020
 */
data class GetTopAnimeParam(val subType: String)
class GetTopAnimeUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<GetTopAnimeParam, List<Anime>>(dispatcher) {
    override suspend fun execute(para: GetTopAnimeParam): List<Anime> {
        return repository.getTopAnime(para.subType).data
            .map {
                AnimeDataMapper().map(it)
            }.filter {
                it.isValid()
            }
    }
}