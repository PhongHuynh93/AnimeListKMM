package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.domain.Result
import com.wind.animelist.shared.domain.data
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.domain.usecase.GetTopAnimeUseCase
import com.wind.animelist.shared.domain.usecase.GetTopMangaParam
import com.wind.animelist.shared.domain.usecase.GetTopMangaUseCase
import com.wind.animelist.shared.util.API_RATE_LIMIT_TIME
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import com.wind.animelist.shared.viewmodel.LoadState.NotLoading.Companion.Complete
import com.wind.animelist.shared.viewmodel.model.Divider
import com.wind.animelist.shared.viewmodel.model.Home
import com.wind.animelist.shared.viewmodel.model.MangaList
import com.wind.animelist.shared.viewmodel.model.Title
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

/**
 * Created by Phong Huynh on 10/6/2020
 */
@ExperimentalCoroutinesApi
class HomeViewModel(
    private val getTopMangaUseCase: GetTopMangaUseCase,
    private val getTopAnimeUseCase: GetTopAnimeUseCase
): BaseViewModel() {
    private val _data = MutableStateFlow<List<Home>?>(null)
    val data: CFlow<List<Home>> get() = _data.filterNotNull().asCommonFlow()
    private var list = mutableListOf<Home>()
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: CFlow<LoadState> get() = _loadState.filterNotNull().asCommonFlow()

    // TODO: 10/6/2020 handle error and loading here
    init {
        clearState()
        // note - rate limited - 2 requests/1s
        clientScope.launch {
            loadAndShowData(listOf(
                async {
                    getTopMangaUseCase(GetTopMangaParam("manga")) to "Top Manga"
                },
                async {
                    getTopMangaUseCase(GetTopMangaParam("novels")) to "Top Novel"
                }
            ))
            delay(API_RATE_LIMIT_TIME)
            loadAndShowData(listOf(
                async {
                    getTopMangaUseCase(GetTopMangaParam("oneshots")) to "Top One Shot"
                },
                async {
                    getTopMangaUseCase(GetTopMangaParam("doujin")) to "Top Doujin"
                }
            ))
            delay(API_RATE_LIMIT_TIME)
            loadAndShowData(listOf(
                async {
                    getTopMangaUseCase(GetTopMangaParam("manhwa")) to "Top Manhwa"
                },
                async {
                    getTopMangaUseCase(GetTopMangaParam("manhua")) to "Top Manhua"
                }
            ))
            _loadState.value = Complete

        }
    }

    private fun clearState() {
        list.clear()
    }

    private suspend fun loadAndShowData(list: List<Deferred<Pair<Result<List<Manga>>, String>>>) {
        val listHome = mutableListOf(*this.list.toTypedArray())
        list.awaitAll().let { list ->
            for (item in list) {
                item.first.data?.let {
                    listHome.add(Divider)
                    // TODO: 10/6/2020 find the workaround for R in android and ios
                    listHome.add(Title(item.second))
                    listHome.add(MangaList(it))
                }
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
        TODO("Not yet implemented")
    }
}