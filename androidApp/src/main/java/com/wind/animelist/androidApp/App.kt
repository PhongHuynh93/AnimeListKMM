package com.wind.animelist.androidApp

import android.app.Application
import com.wind.animelist.androidApp.di.appModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

/**
 * Created by Phong Huynh on 9/26/2020
 */
class App : Application(), DIAware {
    override fun onCreate() {
        super.onCreate()
    }

    override val di = DI {
        import(androidXModule(this@App))
        import(appModule)
    }
}