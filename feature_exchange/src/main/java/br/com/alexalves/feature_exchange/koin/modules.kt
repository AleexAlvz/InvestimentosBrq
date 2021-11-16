package br.com.alexalves.feature_exchange.koin

import br.com.alexalves.feature_exchange.repository.ExchangeDataSource
import br.com.alexalves.feature_exchange.ui.viewmodels.ExchangeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val exchangeModule = module {
    viewModel<ExchangeViewModel> { ExchangeViewModel(get<ExchangeDataSource>()) }
    factory<ExchangeDataSource> { ExchangeDataSource(get()) }
}