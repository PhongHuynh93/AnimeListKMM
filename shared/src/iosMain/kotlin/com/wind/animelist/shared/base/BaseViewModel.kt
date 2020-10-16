package com.wind.animelist.shared.base

import com.wind.animelist.shared.di.bgDispatcherTag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

/**
 * Created by Phong Huynh on 10/6/2020
 */
actual open class BaseViewModel actual constructor(): KoinComponent {
    private val viewModelJob = SupervisorJob()
    private val ioDispatcher: CoroutineDispatcher by inject(qualifier = named(bgDispatcherTag))
    private val viewModelScope: CoroutineScope = CoroutineScope(ioDispatcher + viewModelJob)

    actual val clientScope: CoroutineScope = viewModelScope

    protected actual open fun onCleared() {
        viewModelJob.cancelChildren()
    }
}