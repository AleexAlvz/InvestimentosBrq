package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.OperateUser
import br.com.alexalves.investimentosbrq.model.User

interface ExchangeRepository {

    fun searchUser(
        userId: Long,
        callBackSearchUser: (result: OperateUser) -> Unit
    )

    fun updateUser(
        user: User,
        callBackUpdateUser: (result: OperateUser) -> Unit
    )

    fun searchCurrencies(
        whenSucess: ((currencies: List<Currency>) -> Unit),
        whenFails: ((error: Exception) -> Unit)
    )

}
