package com.wind.animelist.androidApp.di

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.wind.animelist.androidApp.home.HomeAdapter
import com.wind.animelist.androidApp.home.HomeAnimeHozAdapter
import com.wind.animelist.androidApp.home.HomeMangaHozAdapter
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider

/**
 * Created by Phong Huynh on 10/4/2020
 */
fun homeModule(frag: Fragment) = DI.Module("home") {
    bind<HomeAdapter>() with provider {
        val applicationContext = frag.requireContext().applicationContext
        HomeAdapter(applicationContext, Glide.with(frag))
    }
}