package com.wind.animelist.androidApp.di

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.wind.animelist.androidApp.home.HomeAdapter
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import com.wind.animelist.androidApp.adapter.FooterAdapter
import com.wind.animelist.androidApp.adapter.HeaderAdapter
import util.loadmore.LoadMoreHelper

/**
 * Created by Phong Huynh on 10/4/2020
 */
fun homeModule(frag: Fragment) = DI.Module("home") {
    bind<HomeAdapter>() with provider {
        val applicationContext = frag.requireContext().applicationContext
        HomeAdapter(applicationContext, Glide.with(frag))
    }
    bind<FooterAdapter>() with provider {
        FooterAdapter()
    }
    bind<HeaderAdapter>() with provider {
        HeaderAdapter()
    }
    bind<LoadMoreHelper>() with provider {
        LoadMoreHelper(frag.childFragmentManager)
    }
}