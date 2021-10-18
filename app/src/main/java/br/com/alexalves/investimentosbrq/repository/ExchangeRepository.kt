package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.User

interface ExchangeRepository {

    suspend fun searchUser(userId: Long): User

    suspend fun updateUser(user: User)

    suspend fun searchCurrencies(): List<Currency>

}
