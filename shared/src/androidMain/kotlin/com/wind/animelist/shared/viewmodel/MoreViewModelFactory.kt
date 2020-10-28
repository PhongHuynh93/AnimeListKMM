package com.wind.animelist.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DI

/**
 * Created by Tron Nguyen on 10/8/2020
 */
class MoreViewModelFactory(val di: DI, val type : String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoreViewModel::class.java)) {
            return MoreViewModel(di, type) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}