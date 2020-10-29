package com.wind.animelist.androidApp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wind.animelist.androidApp.model.TitleManga
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga
import util.Event

/**
 * Created by Phong Huynh on 10/6/2020
 */
class NavViewModel : ViewModel() {
    val goToAnime: MutableLiveData<Event<Anime>> by lazy {
        MutableLiveData<Event<Anime>>()
    }
    val goToManga: MutableLiveData<Event<Manga>> by lazy {
        MutableLiveData<Event<Manga>>()
    }
    val goToMoreManga: MutableLiveData<Event<TitleManga>> by lazy {
        MutableLiveData<Event<TitleManga>>()
    }
}