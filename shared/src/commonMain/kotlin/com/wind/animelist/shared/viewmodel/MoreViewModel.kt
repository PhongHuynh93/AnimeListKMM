package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.domain.Result
import com.wind.animelist.shared.domain.data
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.BaseModel
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.domain.successOr
import com.wind.animelist.shared.domain.usecase.GetTopAnimeParam
import com.wind.animelist.shared.domain.usecase.GetTopAnimeUseCase
import com.wind.animelist.shared.domain.usecase.GetTopMangaParam
import com.wind.animelist.shared.domain.usecase.GetTopMangaUseCase
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import com.wind.animelist.shared.viewmodel.model.Divider
import com.wind.animelist.shared.viewmodel.model.Title
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import org.kodein.di.DI
import org.kodein.di.instance

/**
 * Created by Phong Huynh on 10/6/2020
 */
@ExperimentalCoroutinesApi
class MoreViewModel(val di: DI, val type : String): BaseViewModel() {
    val getTopMangaUseCase: GetTopMangaUseCase by di.instance()
    val getTopAnimeUseCase: GetTopAnimeUseCase by di.instance()


    private val _data = MutableStateFlow<List<BaseModel>?>(null)
    val data: CFlow<List<BaseModel>> get() = _data.filterNotNull().asCommonFlow()
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: CFlow<LoadState> get() = _loadState.filterNotNull().asCommonFlow()
    // TODO: 10/6/2020 handle error and loading here
    init {
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