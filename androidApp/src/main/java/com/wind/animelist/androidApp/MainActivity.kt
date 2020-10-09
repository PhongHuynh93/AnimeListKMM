package com.wind.animelist.androidApp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.wind.animelist.androidApp.ui.home.HomeFragment
import com.wind.animelist.shared.viewmodel.NavViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(R.layout.fragment) {
    val vmNav by viewModels<NavViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.root, HomeFragment.newInstance())
            }
        }
        vmNav.goToDetailManga.observe {

        }
    }


}