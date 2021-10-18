package br.com.alexalves.investimentosbrq.di

import androidx.room.Room
import br.com.alexalves.investimentosbrq.database.AppDatabase
import br.com.alexalves.investimentosbrq.database.UserDAO
import br.com.alexalves.investimentosbrq.repository.ExchangeDataSource
import br.com.alexalves.investimentosbrq.repository.ExchangeDataSourceWrapper
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosService
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosServiceAPI
import br.com.alexalves.investimentosbrq.viewmodel.ExchangeViewModel
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single<AppDatabase> { Room.databaseBuilder(get(), AppDatabase::class.java, "usuario_db").fallbackToDestructiveMigration().build() }
    single<UserDAO> { get<AppDatabase>().userDAO }
    single<ExchangeDataSource> { ExchangeDataSource(get(), get(), get()) }
    single<ExchangeDataSourceWrapper> { ExchangeDataSourceWrapper() }
    single<InvestimentosService> { InvestimentosServiceAPI().getInvestimentosService() }

    viewModel<HomeViewModel> { HomeViewModel(get<ExchangeDataSource>()) }
    viewModel<ExchangeViewModel> { ExchangeViewModel(get<ExchangeDataSource>())}
}