package br.com.alexalves.base.repository

import br.com.alexalves.base.database.UserDAO
import br.com.alexalves.base.service.InvestimentosService
import br.com.alexalves.models.Currency
import br.com.alexalves.models.User

class HomeDataSource(
    private val userDao: UserDAO,
    private val investimentoServiceAPIWrapper: InvestimentoServiceAPIWrapper,
    private val service: InvestimentosService
) : HomeRepository {

    override suspend fun searchUser(userId: Long): User = userDao.searchUser(userId)

    override suspend fun saveUser(user: User) = userDao.saveUser(user)

    override suspend fun searchCurrencies(): List<Currency> = investimentoServiceAPIWrapper
        .filterCurrencies(service.getService().execute().body())

}