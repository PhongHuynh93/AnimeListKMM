package com.wind.animelist.shared.domain.usecase

import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.UseCase
import com.wind.animelist.shared.domain.mapper.impl.MoreInfoDataMapper
import com.wind.animelist.shared.domain.model.MoreInfo
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by Phong Huynh on 10/10/2020
 */
data class GetMoreInfoParam(val id: Int)
class GetMoreInfoUseCase constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: Repository
) : UseCase<GetMoreInfoParam, MoreInfo>(dispatcher) {
    override suspend fun execute(parameters: GetMoreInfoParam): MoreInfo {
        return repository.getMoreInfo(parameters.id).let {
            MoreInfoDataMapper().map(it)
        }
    }
}