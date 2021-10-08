package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.investimentosbrq.consts.OperationConsts
import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.model.*
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosServiceAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExchangeDataSource(
    private val userDao: UsuarioDao
) : ExchangeRepository {
    private val service = InvestimentosServiceAPI().getInvestimentosService()

    override fun searchUser(
        userId: Long,
        callBackSearchUser: (result: OperateUser) -> Unit
    ) {
        CoroutineScope(IO).launch {
            try {
                val user = userDao.searchUser(id = userId)
                withContext(Main) {
                    callBackSearchUser.invoke(OperateUser.Success(user))
                }
            } catch (error: Exception) {
                withContext(Main) {
                    callBackSearchUser.invoke(OperateUser.Error(Exception(OperationConsts().FAILS_IN_SEARCH_USER)))
                }
            }
        }
    }

    override fun updateUser(
        user: User,
        callBackUpdateUser: (result: OperateUser) -> Unit
    ) {
        CoroutineScope(IO).launch {
            try {
                userDao.updateUser(user)
                withContext(Main){
                    callBackUpdateUser.invoke(OperateUser.Success(user))
                }
            } catch (error: Exception) {
                withContext(Main){
                    callBackUpdateUser.invoke(OperateUser.Error(java.lang.Exception(OperationConsts().FAILS_IN_SEARCH_USER)))
                }
            }
        }
    }

    override fun searchCurrencies(
        whenSucess: ((currencies: List<Currency>) -> Unit),
        whenFails: ((error: Exception) -> Unit)
    ) {
        val callService = service.getService()
        CoroutineScope(IO).launch {
            try {
                val service = callService.execute().body()
                val foundCurrencies = configureAndFilterCurrencies(service)
                withContext(Main) {
                    whenSucess.invoke(foundCurrencies)
                }
            } catch (error: Exception) {
                withContext(Main){
                    whenFails.invoke(error)
                }
            }
        }
    }

    private fun configureAndFilterCurrencies(service: ServiceInvestimentos?): List<Currency> {
        var currencies = arrayListOf<Currency>()
        service?.results?.currencies?.let {
            val ars = it.ars
            ars.setAbbreviationAndSource(it.source)
            val aud = it.aud
            aud.setAbbreviationAndSource(it.source)
            val btc = it.btc
            btc.setAbbreviationAndSource(it.source)
            val cad = it.cad
            cad.setAbbreviationAndSource(it.source)
            val cny = it.cny
            cny.setAbbreviationAndSource(it.source)
            val eur = it.eur
            eur.setAbbreviationAndSource(it.source)
            val gbp = it.gbp
            gbp.setAbbreviationAndSource(it.source)
            val jpy = it.jpy
            jpy.setAbbreviationAndSource(it.source)
            val usd = it.usd
            usd.setAbbreviationAndSource(it.source)
            val configuredCurrencies = listOf(ars, aud, btc, cad, cny, eur, gbp, jpy, usd)
            for (currency in configuredCurrencies) {
                if (currency.sell != null && currency.buy != null) {
                    currencies.add(currency)
                }
            }
        }
        return currencies
    }

}