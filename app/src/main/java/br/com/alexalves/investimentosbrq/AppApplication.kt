package br.com.alexalves.investimentosbrq

import android.app.Application
import br.com.alexalves.base.koin.baseModules
import br.com.alexalves.feature_exchange.koin.exchangeModule
import br.com.alexalves.investimentosbrq.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(homeModule+ exchangeModule+ baseModules)
        }
    }
}