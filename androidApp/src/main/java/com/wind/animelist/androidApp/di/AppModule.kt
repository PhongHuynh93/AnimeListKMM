package com.wind.animelist.androidApp.di

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.wind.animelist.androidApp.ui.home.HomeAdapter
import com.wind.animelist.androidApp.ui.adapter.LoadingAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleHeaderAdapter
import com.wind.animelist.androidApp.ui.detail.DetailMangaAdapter
import com.wind.animelist.androidApp.ui.detail.DetailMangaHeaderAdapter
import com.wind.animelist.shared.viewmodel.DetailMangaViewModel
import com.wind.animelist.shared.viewmodel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import util.loadmore.LoadMoreHelper

/**
 * Created by Phong Huynh on 10/4/2020
 */
@ExperimentalCoroutinesApi
val appModule = module {
    factory { (frag: Fragment) ->
        val applicationContext = frag.requireContext().applicationContext
        HomeAdapter(applicationContext, Glide.with(frag))
    }
    factory { (_: Fragment) ->
        LoadingAdapter()
    }
    factory { (_: Fragment) ->
        TitleHeaderAdapter()
    }
    factory { (frag: Fragment) ->
        LoadMoreHelper(frag.childFragmentManager)
    }
    factory { (frag: Fragment) ->
        DetailMangaAdapter(Glide.with(frag))
    }
    factory { (frag: Fragment) ->
        DetailMangaHeaderAdapter(Glide.with(frag))
    }
    viewModel { HomeViewModel() }
    viewModel { DetailMangaViewModel() }
}