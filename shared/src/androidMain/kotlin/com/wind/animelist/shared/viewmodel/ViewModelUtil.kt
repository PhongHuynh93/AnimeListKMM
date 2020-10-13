package com.wind.animelist.shared.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wind.animelist.shared.domain.model.Anime
import com.wind.animelist.shared.domain.model.Manga
import util.DisableDragBottomSheetBehavior
import util.Event

/**
 * Created by Phong Huynh on 10/6/2020
 */
class NavViewModel : ViewModel() {
    val goToAnime: MutableLiveData<Event<Anime>> by lazy {
        MutableLiveData<Event<Anime>>()
    }
    val goToManga: MutableLiveData<Event<Pair<View, Manga>>> by lazy {
        MutableLiveData<Event<Pair<View, Manga>>>()
    }
}