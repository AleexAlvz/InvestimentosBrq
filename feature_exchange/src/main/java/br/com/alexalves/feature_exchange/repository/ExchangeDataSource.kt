package br.com.alexalves.feature_exchange.repository

import br.com.alexalves.base.database.UserDAO
import br.com.alexalves.models.User

class ExchangeDataSource(
    private val userDao: UserDAO,
) : ExchangeRepository {

    override suspend fun searchUser(userId: Long): User = userDao.searchUser(userId)

    override suspend fun updateUser(user: User) = userDao.updateUser(user)

}