package com.wind.animelist.androidApp.di

import org.kodein.di.DI

/**
 * Created by Phong Huynh on 9/30/2020
 */
val appModule = DI.Module("app") {
    import(dataModule)
    import(domainModule)
}