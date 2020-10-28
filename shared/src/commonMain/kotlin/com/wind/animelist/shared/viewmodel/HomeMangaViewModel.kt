package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.base.ioDispatcher
import com.wind.animelist.shared.domain.Result
import com.wind.animelist.shared.domain.data
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.domain.usecase.GetTopMangaParam
import com.wind.animelist.shared.domain.usecase.GetTopMangaUseCase
import com.wind.animelist.shared.util.API_RATE_LIMIT_TIME
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import com.wind.animelist.shared.viewmodel.LoadState.NotLoading.Companion.Complete
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
    private var doing: Boolean = false
    private val getTopMangaUseCase: GetTopMangaUseCase by inject()
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
            loadAndShowData(
                listOf(
                    (getTopMangaUseCase(GetTopMangaParam("manga")) to "Top Manga"),
                    (getTopMangaUseCase(GetTopMangaParam("novels")) to "Top Novel")
                )
            )
            delay(API_RATE_LIMIT_TIME)
            loadAndShowData(
                listOf(
                    (getTopMangaUseCase(GetTopMangaParam("oneshots")) to "Top One Shot"),
                )
            )
            delay(API_RATE_LIMIT_TIME)
            doing = false
        }
    }

    private fun clearState() {
        list.clear()
    }

    private fun loadAndShowData(list: List<Pair<Result<List<Manga>>, String>>) {
        val listHome = mutableListOf(*this.list.toTypedArray())
        for (item in list) {
            item.first.data?.let {
                // TODO: 10/6/2020 find the workaround for R in android and ios
                listHome.add(MangaList(it.shuffled(), item.second))
            }
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
        clientScope.launch(ioDispatcher) {
            loadAndShowData(
                listOf(
                    (getTopMangaUseCase(GetTopMangaParam("manhwa")) to "Top Manhwa"),
                    (getTopMangaUseCase(GetTopMangaParam("manhua")) to "Top Manhua")
                )
            )
            _loadState.value = Complete
            doing = false
        }
    }
}