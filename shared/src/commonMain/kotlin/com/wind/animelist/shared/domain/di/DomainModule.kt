package com.wind.animelist.shared.domain.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

//const val defaultDispatcherTag = "DefaultDispatcher"
const val ioDispatcherTag = "IoDispatcher"
const val mainDispatcherTag = "MainDispatcher"
const val mainImmediateDispatcherTag = "MainImmediateDispatcher"
val domainModule = DI.Module("domain") {
//    bind<CoroutineDispatcher>(defaultDispatcherTag) with singleton {
//        Dispatchers.Default
//    }
    // FIXME: 10/5/2020 temperately disable io dispatcher because multiplatform dont have that field
    bind<CoroutineDispatcher>(ioDispatcherTag) with singleton {
//        Dispatchers.IO
        Dispatchers.Default
    }
    bind<CoroutineDispatcher>(mainDispatcherTag) with singleton {
        Dispatchers.Main
    }
    bind<CoroutineDispatcher>(mainImmediateDispatcherTag) with singleton {
        Dispatchers.Main.immediate
    }
}