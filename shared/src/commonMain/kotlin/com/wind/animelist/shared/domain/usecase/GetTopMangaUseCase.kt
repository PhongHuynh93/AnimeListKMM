package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.MangaDataMapper
import com.wind.animelist.shared.domain.model.Manga
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 9/26/2020
 */
class GetTopMangaUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<Unit, List<Manga>>(dispatcher) {
    override suspend fun execute(parameters: Unit): List<Manga> {
        return repository.getTopManga().data
            .map {
                MangaDataMapper().map(it)
            }.filter {
                it.isValid()
            }
    }
}
