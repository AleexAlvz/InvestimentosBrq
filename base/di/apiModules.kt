package br.com.alexalves.di

import br.com.alexalves.api.InvestimentosService
import br.com.alexalves.api.InvestimentosServiceAPI
import org.koin.dsl.module

val apiModule = module {
    single<InvestimentosService> {
        InvestimentosServiceAPI()
            .getInvestimentosService()
    }
}