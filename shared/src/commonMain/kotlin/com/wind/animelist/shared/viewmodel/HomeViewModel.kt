package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.domain.Result
import com.wind.animelist.shared.domain.data
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.domain.usecase.GetTopAnimeUseCase
import com.wind.animelist.shared.domain.usecase.GetTopMangaParam
import com.wind.animelist.shared.domain.usecase.GetTopMangaUseCase
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
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
    fun loadMoreManga() {
        TODO("Not yet implemented")
    }

    val getTopMangaUseCase: GetTopMangaUseCase by di.instance()
    val getTopAnimeUseCase: GetTopAnimeUseCase by di.instance()

    private val _data = MutableStateFlow<List<HomeItem>?>(null)
    val data: CFlow<List<HomeItem>> get() = _data.filterNotNull().asCommonFlow()

    // TODO: 10/6/2020 handle error and loading here
    init {
        clientScope.launch {
            val topMangaListDeferredList = mutableListOf<Deferred<Result<List<Manga>>>>()
                .apply {
                    add(async {
                        getTopMangaUseCase(GetTopMangaParam("manga"))
                    })
                    add(async {
                        getTopMangaUseCase(GetTopMangaParam("novels"))
                    })
                    add(async {
                        getTopMangaUseCase(GetTopMangaParam("oneshots"))
                    })
                    add(async {
                        getTopMangaUseCase(GetTopMangaParam("doujin"))
                    })
                    add(async {
                        getTopMangaUseCase(GetTopMangaParam("manhwa"))
                    })
                    add(async {
                        getTopMangaUseCase(GetTopMangaParam("manhua"))
                    })
                }
            val listHome = mutableListOf<HomeItem>()
            topMangaListDeferredList.awaitAll().let { list ->
                for (item in list) {
                    item.data?.let {
                        listHome.add(Divider)
                        // TODO: 10/6/2020 find the workaround for R in android and ios
                        listHome.add(Title("Top manga"))
                        listHome.add(HomeManga(it))
                    }
                }

            }
//            topAnimeListDeferred.await().apply {
//                data?.let {
//                    list.add(Divider)
//                    list.add(Title(R.string.top_anime))
//                    list.add(HomeAnime(it))
//                }
//            }
            if (listHome.isEmpty()) {
                // TODO: 9/28/2020 show no data
            } else {
                _data.value = listHome
            }
        }
    }
}