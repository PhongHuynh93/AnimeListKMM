package com.wind.animelist.androidApp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialContainerTransform
import com.wind.animelist.androidApp.home.MoreMangaFragment
import com.wind.animelist.androidApp.ui.detail.DetailMangaFragment
import com.wind.animelist.androidApp.ui.home.MainFragment
import com.wind.animelist.androidApp.viewmodel.NavViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import util.EventObserver
import util.fullScreen
import util.useAnim

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(R.layout.fragment) {
    val vmNav by viewModels<NavViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        if (savedInstanceState == null) {
            supportFragmentManager.commit(true) {
                add(R.id.root, MainFragment.newInstance())
            }
        }
        vmNav.goToManga.observe(this, EventObserver {
            supportFragmentManager.commit(true) {
                useAnim()
                replace(R.id.root, DetailMangaFragment.newInstance(it).apply {
                    sharedElementEnterTransition = MaterialContainerTransform()
                })
                addToBackStack(null)
            }
        })
        vmNav.goToMoreManga.observe(this, EventObserver {
            supportFragmentManager.commit(true) {
                useAnim()
                replace(R.id.root, MoreMangaFragment.newInstance(it).apply {
                    sharedElementEnterTransition = MaterialContainerTransform()
                })
                addToBackStack(null)
            }
        })
    }
}