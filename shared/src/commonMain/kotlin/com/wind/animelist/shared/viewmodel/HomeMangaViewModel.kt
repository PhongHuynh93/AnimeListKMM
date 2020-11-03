package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.base.ioDispatcher
import com.wind.animelist.shared.base.log
import com.wind.animelist.shared.domain.Result
import com.wind.animelist.shared.domain.model.TypeAPI
import com.wind.animelist.shared.domain.usecase.GetMangaHomeParam
import com.wind.animelist.shared.domain.usecase.GetMangaHomeUseCase
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import com.wind.animelist.shared.viewmodel.LoadState.NotLoading.Companion.Complete
import com.wind.animelist.shared.viewmodel.LoadState.NotLoading.Companion.Incomplete
import com.wind.animelist.shared.viewmodel.model.Home
import com.wind.animelist.shared.viewmodel.model.MangaList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by Phong Huynh on 10/6/2020
 */

@ExperimentalCoroutinesApi
class HomeMangaViewModel : BaseViewModel(), KoinComponent {
    private var getDataJob: Job? = null
    private val getMangaHomeUseCase: GetMangaHomeUseCase by inject()
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
        doing = true
        // note - rate limited - 2 requests/1s
        getDataJob = clientScope.launch(ioDispatcher) {
            ensureActive()
            _loadState.value = LoadState.Loading
            loadAndShowData(true)
            ensureActive()
            doing = false
            _loadStateRefresh.value = Incomplete
            log("getData return $list loadstate ${_loadState.value}")
        }
    }

    private fun clearState() {
        list = mutableListOf()
        doing = false
    }

    private suspend fun loadAndShowData(isRefreshing: Boolean = false) {
        log("loadAndShowData start")
        val listHome = mutableListOf(*this.list.toTypedArray())
        when (val item = getMangaHomeUseCase(GetMangaHomeParam(isRefreshing))) {
            is Result.Success -> {
                item.data.let { response ->
                    // TODO: 10/6/2020 find the workaround for R in android and ios
                    val title = when (response.loadMoreInfo.type) {
                        TypeAPI.TopManga -> "Top Manga"
                        TypeAPI.TopNovels -> "Top Novels"
                        TypeAPI.TopOneShots -> "Top One Shots"
                        TypeAPI.TopManhwa -> "Top Manhwa"
                        TypeAPI.TopManhu -> "Top Manhu"
                        else -> {
                            ""
                        }
                    }
                    listHome.add(MangaList(title, response.data.shuffled(), response.loadMoreInfo, response.page))
                    if (listHome.isEmpty()) {
                        // TODO: 9/28/2020 show no data
                    } else {
                        log("result success ${_data.value.hashCode()} ${listHome.hashCode()}")
                        _data.value = listHome
                        log("result success after ${_data.value.hashCode()}")
                    }
                    this.list = listHome
                    if (!response.isMore) {
                        this._loadState.value = Complete
                    }
                    log("loadAndShowData return $list")
                }
            }
            is Result.Error -> {
                // TODO: 11/1/2020 check error here
                log("onerror isrefreshing $isRefreshing ${item.exception}")
                if (isRefreshing) {
                    // remove all the old data and then show error to handle retry
                    // TODO: 11/2/2020 testing why not emit this value
                    val emptyList = mutableListOf<Home>()
                    log("result error _data.value ${_data.value.hashCode()} and list ${emptyList.hashCode()}")
                    _data.value = emptyList
                    this.list = emptyList
                    log("emit empty list $list")
                }
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
            ensureActive()
            loadAndShowData()
            ensureActive()
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
        _loadState.value = LoadState.Loading
        loadMore()
    }
}