package br.com.alexalves.base.repository

import br.com.alexalves.base.database.UserDAO
import br.com.alexalves.base.service.InvestimentosService

class HomeDataSource(
    private val userDao: UserDAO,
    private val investimentoServiceAPIWrapper: InvestimentoServiceAPIWrapper,
    private val service: InvestimentosService
) : HomeRepository {

    override suspend fun searchUser(userId: Long): br.com.alexalves.models.User = userDao.searchUser(userId)

    override suspend fun saveUser(user: br.com.alexalves.models.User) = userDao.saveUser(user)

    override suspend fun searchCurrencies(): List<br.com.alexalves.models.Currency> = investimentoServiceAPIWrapper
        .filterCurrencies(service.getService().execute().body())

}