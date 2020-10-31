package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.base.ioDispatcher
import com.wind.animelist.shared.domain.data
import com.wind.animelist.shared.domain.model.LoadMoreInfo
import com.wind.animelist.shared.domain.model.TypeAPI
import com.wind.animelist.shared.domain.usecase.GetTopAnimeParam
import com.wind.animelist.shared.domain.usecase.GetTopAnimeUseCase
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import com.wind.animelist.shared.viewmodel.LoadState.NotLoading.Companion.Complete
import com.wind.animelist.shared.viewmodel.model.AnimeList
import com.wind.animelist.shared.viewmodel.model.Home
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
class HomeAnimeViewModel: BaseViewModel(), KoinComponent {
    private var doing: Boolean = false
    private var finishGetSpecial: Boolean = false
    private var finishGetMovie: Boolean = false
    private var finishGetData: Boolean = false
    private val getTopAnimeUseCase: GetTopAnimeUseCase by inject()
    private val _data = MutableStateFlow<List<Home>?>(null)
    val data: CFlow<List<Home>> get() = _data.filterNotNull().asCommonFlow()
    private var list = mutableListOf<Home>()
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: CFlow<LoadState> get() = _loadState.filterNotNull().asCommonFlow()

    // TODO: 10/6/2020 handle error and loading here
    init {
        doing = true
        clearState()
        // note - rate limited - 2 requests/1s
        clientScope.launch(ioDispatcher) {
            loadAndShowData(TypeAPI.TopAiring, true)
            loadAndShowData(TypeAPI.TopUpcoming, false)
            finishGetData = true
            doing = false
        }
    }

    private fun clearState() {
        list.clear()
    }

    private suspend fun loadAndShowData(apiType: TypeAPI, isRefreshing: Boolean) {
        val listHome = mutableListOf(*this.list.toTypedArray())
        val item = getTopAnimeUseCase(GetTopAnimeParam(apiType.getType(), 1, false))

        item.data?.let {
            // TODO: 10/6/2020 find the workaround for R in android and ios
            val title = when (apiType) {
                TypeAPI.TopAiring -> "Top Airing"
                TypeAPI.TopUpcoming -> "Top Upcoming"
                TypeAPI.TopTv -> "Top TV"
                TypeAPI.TopMovie -> "Top Movie"
                TypeAPI.TopOva -> "Top Ova"
                TypeAPI.TopSpecial -> "Top Special"
                else -> {
                    ""
                }
            }
            listHome.add(AnimeList(title, it.shuffled(), LoadMoreInfo(apiType.getType()), 1))
        }
        if (listHome.isEmpty()) {
            // TODO: 9/28/2020 show no data
        } else {
            _data.value = listHome
        }
        this.list = listHome
    }

    fun loadMore() {
        if (doing || _loadState.value == Complete) return
        doing = true
        if (finishGetData) {
            if (!finishGetMovie) {
                clientScope.launch(ioDispatcher) {
                    loadAndShowData(TypeAPI.TopTv, false)
                    loadAndShowData(TypeAPI.TopMovie, false)
                    finishGetMovie = true
                    doing = false
                }
            } else if (!finishGetSpecial) {
                clientScope.launch(ioDispatcher) {
                    loadAndShowData(TypeAPI.TopOva, false)
                    loadAndShowData(TypeAPI.TopSpecial, false)
                    finishGetSpecial = true
                    _loadState.value = Complete
                    doing = false
                }
            } else {
                // TODO: 10/19/2020 load the manga news here
            }
        }
    }
}