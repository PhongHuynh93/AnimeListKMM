package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.domain.successOr
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
class MoreMangaViewModel : BaseViewModel(), KoinComponent {
    val getTopMangaUseCase: GetTopMangaUseCase by inject()

    private val _data = MutableStateFlow<List<Manga>?>(null)
    val data: CFlow<List<Manga>> get() = _data.filterNotNull().asCommonFlow()
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