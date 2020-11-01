package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.base.ioDispatcher
import com.wind.animelist.shared.base.log
import com.wind.animelist.shared.domain.Result
import com.wind.animelist.shared.domain.model.LoadMoreInfo
import com.wind.animelist.shared.domain.model.TypeAPI
import com.wind.animelist.shared.domain.usecase.GetTopMangaParam
import com.wind.animelist.shared.domain.usecase.GetTopMangaUseCase
import com.wind.animelist.shared.util.API_RATE_LIMIT_TIME
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import com.wind.animelist.shared.viewmodel.LoadState.NotLoading.Companion.Complete
import com.wind.animelist.shared.viewmodel.LoadState.NotLoading.Companion.Incomplete
import com.wind.animelist.shared.viewmodel.model.Home
import com.wind.animelist.shared.viewmodel.model.MangaList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by Phong Huynh on 10/6/2020
 */

@ExperimentalCoroutinesApi
class HomeMangaViewModel : BaseViewModel(), KoinComponent {
    private var page: Int = 0
    private var getDataJob: Job? = null
    private val getTopMangaUseCase: GetTopMangaUseCase by inject()
    private val _data = MutableStateFlow<List<Home>?>(null)
    val data: CFlow<List<Home>> get() = _data.filterNotNull().asCommonFlow()
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: CFlow<LoadState> get() = _loadState.filterNotNull().asCommonFlow()

    private val _loadStateRefresh = MutableStateFlow<LoadState>(Incomplete)
    val loadStateRefresh: CFlow<LoadState> get() = _loadStateRefresh.filterNotNull().asCommonFlow()

    // internal state
    private var doing: Boolean = false
    private var list = mutableListOf<Home>()

    init {
        getData()
    }

    private fun getData() {
        log("getdata start")
        doing = true
        // note - rate limited - 2 requests/1s
        getDataJob = clientScope.launch(ioDispatcher) {
            ensureActive()
            _loadState.value = LoadState.Loading
            loadAndShowData(TypeAPI.TopManga, true)
            ensureActive()
            loadAndShowData(TypeAPI.TopNovels)
            ensureActive()
            doing = false
            _loadStateRefresh.value = Incomplete
            log("getData return $list loadstate ${_loadState.value}")
        }
    }

    private fun clearState() {
        page = 0
        list.clear()
        doing = false
    }

    private suspend fun loadAndShowData(apiType: TypeAPI, isRefreshing: Boolean = false) {
        log("loadAndShowData start")
        val listHome = mutableListOf(*this.list.toTypedArray())
        when (val item = getTopMangaUseCase(GetTopMangaParam(apiType.getType(), 1, isRefreshing))) {
            is Result.Success -> {
                item.data.let {
                    // TODO: 10/6/2020 find the workaround for R in android and ios
                    val title = when (apiType) {
                        TypeAPI.TopManga -> "Top Manga"
                        TypeAPI.TopNovels -> "Top Novels"
                        TypeAPI.TopOneShots -> "Top One Shots"
                        TypeAPI.TopManhwa -> "Top Manhwa"
                        TypeAPI.TopManhu -> "Top Manhu"
                        else -> {
                            ""
                        }
                    }
                    listHome.add(MangaList(title, it.shuffled(), LoadMoreInfo(apiType.getType()), 1))
                    if (listHome.isEmpty()) {
                        // TODO: 9/28/2020 show no data
                    } else {
                        _data.value = listHome
                    }
                    this.list = listHome
                    log("loadAndShowData return $list")
                }
            }
            is Result.Error -> {
                // TODO: 11/1/2020 check error here
                log("onerror isrefreshing $isRefreshing ${item.exception}")
                if (isRefreshing) {
                    // remove all the old data and then show error to handle retry
                    this.list = mutableListOf()
                    _data.value = list
                }
                _loadStateRefresh.value = Incomplete
                _loadState.value = LoadState.Error(item.exception)
            }
            Result.Loading -> {
                // not used
            }
        }
    }

    fun loadMore() {
        log("loadmore start $doing ${_loadState.value}")
        if (doing || _loadState.value == Complete || _loadState.value is LoadState.Error) return
        doing = true
        getDataJob = clientScope.launch(ioDispatcher) {
            log("loadmore $list")
            page++
            ensureActive()
            loadAndShowData(TypeAPI.TopOneShots)
            ensureActive()
            delay(API_RATE_LIMIT_TIME)
            ensureActive()
            loadAndShowData(TypeAPI.TopManhwa)
            ensureActive()
            loadAndShowData(TypeAPI.TopManhu)
            ensureActive()
            _loadState.value = Complete
            doing = false
            log("loadmore return loadstate ${_loadState.value}")
        }
    }

    fun refresh() {
        log("########################################################################refresh")
        getDataJob?.cancel()
        _loadStateRefresh.value = LoadState.Loading
        clearState()
        getData()
    }

    fun retry() {
        log("retry")
    }
}