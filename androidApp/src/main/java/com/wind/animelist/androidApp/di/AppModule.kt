package com.wind.animelist.androidApp.di

import com.wind.animelist.shared.data.di.dataModule
import com.wind.animelist.shared.domain.di.domainModule
import org.kodein.di.DI

/**
 * Created by Phong Huynh on 9/30/2020
 */
val appModule = DI.Module("app") {
    import(dataModule)
    import(domainModule)
}