package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.investimentosbrq.database.UserDAO
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.User
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosService

class ExchangeDataSource(
    private val userDao: UserDAO,
    private val service: InvestimentosService,
    private val exchangeDataSourceWrapper: ExchangeDataSourceWrapper
) : ExchangeRepository {

    override suspend fun searchUser(userId: Long): User = userDao.searchUser(userId)

    override suspend fun updateUser(user: User) = userDao.updateUser(user)

    override suspend fun searchCurrencies(): List<Currency> = exchangeDataSourceWrapper
        .filterCurrencies(service.getService().execute().body())
}