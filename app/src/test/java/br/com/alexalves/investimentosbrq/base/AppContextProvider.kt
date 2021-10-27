package br.com.alexalves.investimentosbrq.base

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object AppContextProvider: br.com.alexalves.base.coroutines.CoroutinesContextProvider {

    var coroutinesContextProviderDelegate: br.com.alexalves.base.coroutines.CoroutinesContextProvider? = null

    override val main: CoroutineContext by lazy {
        coroutinesContextProviderDelegate?.main ?: Dispatchers.Main
    }

    override val io: CoroutineContext by lazy {
        coroutinesContextProviderDelegate?.io ?: Dispatchers.IO
    }
}