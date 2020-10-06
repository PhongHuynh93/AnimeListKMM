package com.wind.animelist.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DI

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