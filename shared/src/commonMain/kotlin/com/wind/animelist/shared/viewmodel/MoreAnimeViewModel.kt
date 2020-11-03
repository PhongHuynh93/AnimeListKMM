package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.base.ioDispatcher
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.LoadMoreInfo
import com.wind.animelist.shared.domain.successOr
import com.wind.animelist.shared.domain.usecase.GetTopAnimeParam
import com.wind.animelist.shared.domain.usecase.GetTopAnimeUseCase
import com.wind.animelist.shared.util.API_RATE_LIMIT_TIME
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by Phong Huynh on 10/6/2020
 */
@ExperimentalCoroutinesApi
class MoreAnimeViewModel() : BaseViewModel(), KoinComponent {
    private var currentPage: Int = 0
    val getTopAnimeUseCase: GetTopAnimeUseCase by inject()
    private lateinit var loadMoreInfo: LoadMoreInfo
    private val _data = MutableStateFlow<List<Anime>?>(null)
    val data: CFlow<List<Anime>> get() = _data.filterNotNull().asCommonFlow()
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: CFlow<LoadState> get() = _loadState.filterNotNull().asCommonFlow()
    private var doing: Boolean = false
    private var list = mutableListOf<Anime>()

    fun setInfo(list: List<Anime>, loadMoreInfo: LoadMoreInfo, loadedPage: Int) {
        this.loadMoreInfo = loadMoreInfo
        currentPage = loadedPage
        this.list.addAll(list)
        _data.value = list
    }

    fun loadMore() {
        if (doing || _loadState.value == LoadState.NotLoading.Complete) return
        doing = true
        clientScope.launch(ioDispatcher) {
            currentPage++
            val currentList = mutableListOf(*list.toTypedArray())
            val list = getTopAnimeUseCase(
                GetTopAnimeParam(
                    loadMoreInfo.type.getType(),
                    currentPage,
                    false
                )
            ).successOr(emptyList())
            if (list.isEmpty()) {
                _loadState.value = LoadState.NotLoading.Complete
                doing = false
            } else {
                currentList.addAll(list)
                this@MoreAnimeViewModel.list = currentList
                _data.value = currentList
                delay(API_RATE_LIMIT_TIME)
                doing = false
            }
        }
    }
}