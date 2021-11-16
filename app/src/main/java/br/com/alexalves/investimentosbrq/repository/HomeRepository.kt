package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.models.Currency
import br.com.alexalves.models.User

interface HomeRepository {

    suspend fun searchCurrencies(): List<Currency>
    suspend fun searchUser(userId: Long): User
    suspend fun saveUser(user: User)

}