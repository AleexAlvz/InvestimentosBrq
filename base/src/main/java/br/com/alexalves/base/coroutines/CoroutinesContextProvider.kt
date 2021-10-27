package br.com.alexalves.base.coroutines

import kotlin.coroutines.CoroutineContext

interface CoroutinesContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
}