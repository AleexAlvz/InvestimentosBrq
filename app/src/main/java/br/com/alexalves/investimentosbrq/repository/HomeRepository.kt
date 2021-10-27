package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.User

interface HomeRepository {

    suspend fun searchCurrencies(): List<Currency>
    suspend fun searchUser(userId: Long): User
    suspend fun saveUser(user: User)

}