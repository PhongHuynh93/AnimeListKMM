package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.domain.data
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.domain.successOr
import com.wind.animelist.shared.domain.usecase.GetCharacterInMangaParam
import com.wind.animelist.shared.domain.usecase.GetCharacterInMangaUseCase
import com.wind.animelist.shared.domain.usecase.GetMoreInfoParam
import com.wind.animelist.shared.domain.usecase.GetMoreInfoUseCase
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import com.wind.animelist.shared.viewmodel.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by Phong Huynh on 10/8/2020
 */
@ExperimentalCoroutinesApi
class DetailMangaViewModel : BaseViewModel(), KoinComponent {
    private val getCharacterInMangaUseCase: GetCharacterInMangaUseCase by inject()
    private val getMoreInfoUseCase: GetMoreInfoUseCase by inject()
    private val _data = MutableStateFlow<List<DetailManga>?>(null)
    val data: CFlow<List<DetailManga>> get() = _data.filterNotNull().asCommonFlow()

    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: CFlow<LoadState> get() = _loadState.filterNotNull().asCommonFlow()

    private val page = 1
    private var list = mutableListOf<DetailManga>()

    fun setManga(manga: Manga) {
        clientScope.launch {
            val id = manga.id
            getMoreInfoUseCase.invoke(GetMoreInfoParam(id)).let {
                it.data?.let { moreInfo ->
                    list.add(DetailMangaMoreInfo(manga.title, moreInfo.text))
                    _data.value = listOf(*list.toTypedArray())
                }
            }
            getCharacterInMangaUseCase.invoke(GetCharacterInMangaParam(id)).let {
                it.successOr(emptyList()).let { list ->
                    if (list.isNotEmpty()) {
                        this@DetailMangaViewModel.list.add(DetailMangaCharacter("Character", list))
                        _data.value = listOf(*this@DetailMangaViewModel.list.toTypedArray())
                    }
                }
            }
            _loadState.value = LoadState.NotLoading.Complete
        }

    }
}