package com.wind.animelist.shared.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val mainDispatcher: CoroutineDispatcher = Dispatchers.Main

// TODO: 10/6/2020 watch this again, this is main dispatcher
actual val ioDispatcher: CoroutineDispatcher = Dispatchers.Main