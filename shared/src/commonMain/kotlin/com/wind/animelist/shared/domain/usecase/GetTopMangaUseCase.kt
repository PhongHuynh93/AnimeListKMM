package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.MangaDataMapper
import com.wind.animelist.shared.domain.model.Manga
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 9/26/2020
 */
data class GetTopMangaParam(val subType: String)
class GetTopMangaUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<GetTopMangaParam, List<Manga>>(dispatcher) {
    override suspend fun execute(para: GetTopMangaParam): List<Manga> {
        return repository.getTopManga(para.subType).data
            .map {
                MangaDataMapper().map(it)
            }.filter {
                it.isValid()
            }
    }
}
