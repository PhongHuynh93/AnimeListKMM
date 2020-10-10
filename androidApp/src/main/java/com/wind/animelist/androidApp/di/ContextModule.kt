package com.wind.animelist.androidApp.di

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.wind.animelist.androidApp.ui.home.HomeAdapter
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import com.wind.animelist.androidApp.ui.adapter.LoadingAdapter
import com.wind.animelist.androidApp.ui.adapter.TitleHeaderAdapter
import com.wind.animelist.androidApp.ui.detail.DetailMangaAdapter
import util.loadmore.LoadMoreHelper

/**
 * Created by Phong Huynh on 10/4/2020
 */
fun homeModule(frag: Fragment) = DI.Module("home") {
    bind<HomeAdapter>() with provider {
        val applicationContext = frag.requireContext().applicationContext
        HomeAdapter(applicationContext, Glide.with(frag))
    }
    bind<LoadingAdapter>() with provider {
        LoadingAdapter()
    }
    bind<TitleHeaderAdapter>() with provider {
        TitleHeaderAdapter()
    }
    bind<LoadMoreHelper>() with provider {
        LoadMoreHelper(frag.childFragmentManager)
    }
}

fun detailMangaModule(frag: Fragment) = DI.Module("detailManga") {
    bind<DetailMangaAdapter>() with provider {
        DetailMangaAdapter(Glide.with(frag))
    }
    bind<LoadingAdapter>() with provider {
        LoadingAdapter()
    }
}