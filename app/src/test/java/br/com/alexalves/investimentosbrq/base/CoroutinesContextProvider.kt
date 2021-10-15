package br.com.alexalves.investimentosbrq.base

import kotlin.coroutines.CoroutineContext

interface CoroutinesContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
}