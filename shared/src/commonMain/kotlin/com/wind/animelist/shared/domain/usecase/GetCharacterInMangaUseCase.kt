package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.data.model.NetworkCharacter
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.AnimeDataMapper
import com.wind.animelist.shared.domain.mapper.impl.CharacterDataMapper
import com.wind.animelist.shared.domain.model.Character
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 10/10/2020
 */
data class GetCharacterInMangaParam(val id: Int)
class GetCharacterInMangaUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<GetCharacterInMangaParam, List<Character>>(dispatcher) {

    override suspend fun execute(parameters: GetCharacterInMangaParam): List<Character> {
        return repository.getMangaCharacter(parameters.id).data
            .map {
                CharacterDataMapper().map(it)
            }.filter {
                it.isValid()
            }
    }
}