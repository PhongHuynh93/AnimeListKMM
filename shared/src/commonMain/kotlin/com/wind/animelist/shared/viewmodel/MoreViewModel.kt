package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.domain.model.BaseModel
import com.wind.animelist.shared.domain.successOr
import com.wind.animelist.shared.domain.usecase.GetTopAnimeUseCase
import com.wind.animelist.shared.domain.usecase.GetTopMangaParam
import com.wind.animelist.shared.domain.usecase.GetTopMangaUseCase
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by Phong Huynh on 10/6/2020
 */
@ExperimentalCoroutinesApi
class MoreViewModel(val type: String) : BaseViewModel(), KoinComponent {
    val getTopMangaUseCase: GetTopMangaUseCase by inject()
    val getTopAnimeUseCase: GetTopAnimeUseCase by inject()


    private val _data = MutableStateFlow<List<BaseModel>?>(null)
    val data: CFlow<List<BaseModel>> get() = _data.filterNotNull().asCommonFlow()
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: CFlow<LoadState> get() = _loadState.filterNotNull().asCommonFlow()

    fun getData(type: String) {
        clientScope.launch {
            val list = getTopMangaUseCase(GetTopMangaParam(type)).successOr(emptyList())

            if (list.isEmpty()) {
                // TODO: 9/28/2020 show no data
            } else {
                _data.value = list
            }
        }
    }
}