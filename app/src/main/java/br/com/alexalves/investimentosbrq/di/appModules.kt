package br.com.alexalves.investimentosbrq.di

import androidx.room.Room
import br.com.alexalves.investimentosbrq.database.AppDatabase
import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.repository.ExchangeDataSource
import br.com.alexalves.investimentosbrq.repository.ExchangeRepository
import br.com.alexalves.investimentosbrq.viewmodel.ExchangeViewModel
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single<AppDatabase> { Room.databaseBuilder(get(), AppDatabase::class.java, "usuario_db").fallbackToDestructiveMigration().build() }
    single<UsuarioDao> { get<AppDatabase>().usuarioDao }
    single<ExchangeDataSource> { ExchangeDataSource(get()) }

    viewModel<HomeViewModel> { HomeViewModel(get<ExchangeDataSource>()) }
    viewModel<ExchangeViewModel> { ExchangeViewModel(get<ExchangeRepository>())}
}