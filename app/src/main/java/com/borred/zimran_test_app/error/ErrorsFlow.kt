package com.borred.zimran_test_app.error

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorsFlow @Inject constructor() {

    private val _errorsFlow = Channel<Throwable>()

    suspend fun collectErrors(
        block: suspend (Throwable) -> Unit
    ) {
        _errorsFlow.receiveAsFlow().collectLatest(block)
    }

    suspend fun sendError(error: Throwable) {
        _errorsFlow.send(error)
    }
}
