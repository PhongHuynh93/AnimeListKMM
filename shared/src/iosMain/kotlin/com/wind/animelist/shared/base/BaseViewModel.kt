package com.wind.animelist.shared.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

/**
 * Created by Phong Huynh on 10/6/2020
 */
actual open class BaseViewModel actual constructor() {
//    private val viewModelJob = SupervisorJob()
//    val viewModelScope: CoroutineScope = CoroutineScope(ioDispatcher + viewModelJob)
//
//    actual val clientScope: CoroutineScope = viewModelScope

//    actual val clientScope = GlobalScope
    protected actual open fun onCleared() {
//        viewModelJob.cancelChildren()
    }

    actual val clientScope: CoroutineScope
        get() = GlobalScope
}