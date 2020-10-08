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
import com.wind.animelist.shared.viewmodel.model.HomeItem
import com.wind.animelist.shared.viewmodel.model.HomeManga
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
class HomeViewModel(val di: DI): BaseViewModel() {
    val getTopMangaUseCase: GetTopMangaUseCase by di.instance()
    val getTopAnimeUseCase: GetTopAnimeUseCase by di.instance()
    private val _data = MutableStateFlow<List<HomeItem>?>(null)
    val data: CFlow<List<HomeItem>> get() = _data.filterNotNull().asCommonFlow()
    private var list = mutableListOf<HomeItem>()
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: CFlow<LoadState> get() = _loadState.filterNotNull().asCommonFlow()

    // TODO: 10/6/2020 handle error and loading here
    init {
        clearState()
        // note - rate limited - 2 requests/1s
        clientScope.launch {
            loadAndShowData(listOf(
                async {
                    getTopMangaUseCase(GetTopMangaParam("manga"))
                },
                async {
                    getTopMangaUseCase(GetTopMangaParam("novels"))
                }
            ))
            delay(API_RATE_LIMIT_TIME)
            loadAndShowData(listOf(
                async {
                    getTopMangaUseCase(GetTopMangaParam("oneshots"))
                },
                async {
                    getTopMangaUseCase(GetTopMangaParam("doujin"))
                }
            ))
            delay(API_RATE_LIMIT_TIME)
            loadAndShowData(listOf(
                async {
                    getTopMangaUseCase(GetTopMangaParam("manhwa"))
                },
                async {
                    getTopMangaUseCase(GetTopMangaParam("manhua"))
                }
            ))
            _loadState.value = Complete

        }
    }

    private fun clearState() {
        list.clear()
    }

    private suspend fun loadAndShowData(list: List<Deferred<Result<List<Manga>>>>) {
        val listHome = mutableListOf(*this.list.toTypedArray())
        list.awaitAll().let { list ->
            for (item in list) {
                item.data?.let {
                    listHome.add(Divider)
                    // TODO: 10/6/2020 find the workaround for R in android and ios
                    listHome.add(Title("Top manga"))
                    listHome.add(HomeManga(it))
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