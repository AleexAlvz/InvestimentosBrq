package br.com.alexalves.base.koin

import androidx.room.Room
import br.com.alexalves.base.api.InvestimentoServiceAPI
import br.com.alexalves.base.database.AppDatabase
import br.com.alexalves.base.database.UserDAO
import br.com.alexalves.base.repository.InvestimentoServiceAPIWrapper
import br.com.alexalves.base.service.InvestimentosService
import org.koin.dsl.module

val dbModule = module {
    single<AppDatabase> { Room.databaseBuilder(get(), AppDatabase::class.java, "usuario_db").fallbackToDestructiveMigration().build() }
    single<UserDAO> { get<AppDatabase>().userDAO }
}

val apiModule = module {
    factory<InvestimentosService> { InvestimentoServiceAPI.provideApi(InvestimentosService::class.java) }
    factory<InvestimentoServiceAPIWrapper> { InvestimentoServiceAPIWrapper() }
}

val baseModules = listOf(apiModule, dbModule)