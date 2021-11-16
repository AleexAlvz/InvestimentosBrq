package br.com.alexalves.investimentosbrq.di

import br.com.alexalves.investimentosbrq.repository.HomeDataSource
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel<HomeViewModel> { HomeViewModel(get<HomeDataSource>()) }
    factory<HomeDataSource> { HomeDataSource(get(), get(), get()) }
}


