package com.wind.animelist.androidApp.di

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.wind.animelist.androidApp.home.MoreAdapter
import com.wind.animelist.androidApp.ui.adapter.HomeAnimeAdapter
import com.wind.animelist.androidApp.ui.adapter.HomeMangaAdapter
import com.wind.animelist.androidApp.ui.adapter.LoadingAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleHeaderAdapter
import com.wind.animelist.androidApp.ui.detail.DetailMangaAdapter
import com.wind.animelist.androidApp.ui.detail.DetailMangaHeaderAdapter
import com.wind.animelist.shared.viewmodel.DetailMangaViewModel
import com.wind.animelist.shared.viewmodel.HomeAnimeViewModel
import com.wind.animelist.shared.viewmodel.HomeMangaViewModel
import com.wind.animelist.shared.viewmodel.MoreMangaViewModel
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
        HomeMangaAdapter(applicationContext, Glide.with(frag))
    }
    factory { (frag: Fragment) ->
        val applicationContext = frag.requireContext().applicationContext
        HomeAnimeAdapter(applicationContext, Glide.with(frag))
    }
    factory { (frag: Fragment) ->
        val applicationContext = frag.requireContext().applicationContext
        MoreAdapter(applicationContext, Glide.with(frag))
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
    viewModel { HomeMangaViewModel() }
    viewModel { HomeAnimeViewModel() }
    viewModel { DetailMangaViewModel() }
    viewModel { MoreMangaViewModel() }
}