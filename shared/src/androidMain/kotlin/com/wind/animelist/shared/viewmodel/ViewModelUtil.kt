package com.wind.animelist.shared.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wind.animelist.shared.data.Repository
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga
import org.kodein.di.DI
import org.kodein.di.DIAware

/**
 * Created by Phong Huynh on 10/6/2020
 */
class HomeViewModelFactory(val di: DI) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(di) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DetailMangaViewModelFactory(val di: DI) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailMangaViewModel::class.java)) {
            return DetailMangaViewModel(di) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class NavViewModel : ViewModel() {
    val goToAnime: MutableLiveData<Event<Anime>> by lazy {
        MutableLiveData<Event<Anime>>()
    }
    val goToManga: MutableLiveData<Event<Manga>> by lazy {
        MutableLiveData<Event<Manga>>()
    }
}