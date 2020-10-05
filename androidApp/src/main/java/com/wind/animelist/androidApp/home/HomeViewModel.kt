package com.wind.animelist.androidApp.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wind.animelist.androidApp.R
import com.wind.animelist.androidApp.di.homeVModule
import com.wind.animelist.androidApp.model.*
import com.wind.animelist.shared.domain.data
import com.wind.animelist.shared.domain.usecase.GetTopAnimeUseCase
import com.wind.animelist.shared.domain.usecase.GetTopMangaUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.subDI
import org.kodein.di.instance
import org.kodein.di.android.x.di

/**
 * Created by Phong Huynh on 9/26/2020
 */
class HomeViewModel constructor(app: Application) : AndroidViewModel(app), DIAware {
    override val di by subDI(di()) {
        import(homeVModule)
    }
    val getTopMangaUseCase: GetTopMangaUseCase by instance()
    val getTopAnimeUseCase: GetTopAnimeUseCase by instance()

    private val _data: MutableLiveData<List<HomeItem>> = MutableLiveData()
    val data: LiveData<List<HomeItem>> = _data

    init {
        viewModelScope.launch {
            val topMangaListDeferred = async {
                getTopMangaUseCase(Unit)
            }
            val topAnimeListDeferred = async {
                getTopAnimeUseCase(Unit)
            }
            val list = mutableListOf<HomeItem>()
            topMangaListDeferred.await().apply {
                data?.let {
                    list.add(Divider)
                    list.add(Title(R.string.top_manga))
                    list.add(HomeManga(it))
                }
            }
            topAnimeListDeferred.await().apply {
                data?.let {
                    list.add(Divider)
                    list.add(Title(R.string.top_anime))
                    list.add(HomeAnime(it))
                }
            }
            if (list.isEmpty()) {
                // TODO: 9/28/2020 show no data
            } else {
                _data.value = list
            }
        }
    }
}