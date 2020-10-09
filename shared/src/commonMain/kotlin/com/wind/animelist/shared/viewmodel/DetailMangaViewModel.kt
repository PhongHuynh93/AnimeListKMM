package com.wind.animelist.shared.viewmodel

import com.wind.animelist.shared.base.BaseViewModel
import com.wind.animelist.shared.domain.model.Manga
import com.wind.animelist.shared.util.CFlow
import com.wind.animelist.shared.util.asCommonFlow
import com.wind.animelist.shared.viewmodel.model.DetailManga
import com.wind.animelist.shared.viewmodel.model.DetailMangaHeader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import org.kodein.di.DI

/**
 * Created by Phong Huynh on 10/8/2020
 */
@ExperimentalCoroutinesApi
class DetailMangaViewModel(val di: DI): BaseViewModel() {
    private val _data = MutableStateFlow<List<DetailManga>?>(null)
    val data: CFlow<List<DetailManga>> get() = _data.filterNotNull().asCommonFlow()

    fun setManga(manga: Manga) {
        _data.value = listOf(DetailMangaHeader(manga))
    }
}