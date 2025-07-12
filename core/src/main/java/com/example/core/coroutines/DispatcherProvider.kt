package com.example.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface DispatcherProvider {
    fun io(): CoroutineDispatcher = Dispatchers.IO
    fun default(): CoroutineDispatcher = Dispatchers.Default
    fun main(): CoroutineDispatcher = Dispatchers.Main
    fun mainImmediate(): CoroutineDispatcher = Dispatchers.Main.immediate
    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}

class StandardDispatcherProvider @Inject constructor() : DispatcherProvider
