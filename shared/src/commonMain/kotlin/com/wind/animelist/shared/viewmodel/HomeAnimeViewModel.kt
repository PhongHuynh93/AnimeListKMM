package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.base.ioDispatcher
import com.wind.animelist.shared.domain.Result
import com.wind.animelist.shared.domain.data
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.usecase.GetTopAnimeParam
import com.wind.animelist.shared.domain.usecase.GetTopAnimeUseCase
import com.wind.animelist.shared.domain.usecase.GetTopMangaParam
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import com.wind.animelist.shared.viewmodel.LoadState.NotLoading.Companion.Complete
import com.wind.animelist.shared.viewmodel.model.AnimeList
import com.wind.animelist.shared.viewmodel.model.Home
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by Phong Huynh on 10/6/2020
 */
@ExperimentalCoroutinesApi
class HomeAnimeViewModel: BaseViewModel(), KoinComponent {
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
        clearState()
        // note - rate limited - 2 requests/1s
        clientScope.launch(ioDispatcher) {
            loadAndShowData(listOf(
                (getTopAnimeUseCase(GetTopAnimeParam("airing")) to "Top Airing"),
                (getTopAnimeUseCase(GetTopAnimeParam("upcoming")) to "Top Upcoming")
            ))
            finishGetData = true
        }
    }

    private fun clearState() {
        list.clear()
    }

    private fun loadAndShowData(list: List<Pair<Result<List<Anime>>, String>>) {
        val listHome = mutableListOf(*this.list.toTypedArray())
            for (item in list) {
                item.first.data?.let {
                    // TODO: 10/6/2020 find the workaround for R in android and ios
                    listHome.add(AnimeList(it.shuffled(), item.second))
                }
            }
        if (listHome.isEmpty()) {
            // TODO: 9/28/2020 show no data
        } else {
            _data.value = listHome
        }
        this.list = listHome
    }

    fun loadMoreManga() {
        // TODO("Not yet implemented")
        if (finishGetData) {
            if (!finishGetMovie) {
                clientScope.launch(ioDispatcher) {
                    loadAndShowData(listOf(
                        (getTopAnimeUseCase(GetTopAnimeParam("tv")) to "Top TV"),
                        (getTopAnimeUseCase(GetTopAnimeParam("movie")) to "Top Movie")
                    ))
                    finishGetMovie = true
                }
            } else if (!finishGetSpecial) {
                clientScope.launch(ioDispatcher) {
                    loadAndShowData(listOf(
                        (getTopAnimeUseCase(GetTopAnimeParam("ova")) to "Top Ova"),
                        (getTopAnimeUseCase(GetTopAnimeParam("special")) to "Top Special")
                    ))
                    finishGetSpecial = true
                    _loadState.value = Complete
                }
            } else {
                // TODO: 10/19/2020 load the manga news here
            }
        }
    }
}