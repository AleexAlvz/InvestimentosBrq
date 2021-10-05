package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.ServiceInvestimentos
import br.com.alexalves.investimentosbrq.model.User
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosServiceAPI
import br.com.alexalves.investimentosbrq.utils.CurrencyUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger

class CurrencyRepository(
    private val userDao: UsuarioDao
) {
    private val service = InvestimentosServiceAPI().getInvestimentosService()

    fun searchUser(
        userId: Long,
        whenSucess: ((user: User) -> Unit),
        whenFails: ((error: Exception) -> Unit)
    ) {
        try {
            CoroutineScope(IO).launch {
                val user = userDao.searchUser(id = userId)
                withContext(Main) {
                    whenSucess.invoke(user)
                }
            }
        } catch (error: Exception) {
            whenFails.invoke(error)
        }
    }

    fun updateUser(
        user: User,
        whenSucess: (() -> Unit),
        whenFails: ((error: Exception) -> Unit)
    ) {
        try {
            userDao.updateUser(user)
            whenSucess.invoke()
        } catch (error: Exception){
            whenFails.invoke(error)
        }
    }

    fun searchCurrencies(
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
                whenFails.invoke(error)
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
            val configuredCurrencies = listOf<Currency>(ars, aud, btc, cad, cny, eur, gbp, jpy, usd)
            for (currency in configuredCurrencies) {
                if (currency.sell != null && currency.buy != null) {
                    currencies.add(currency)
                }
            }
        }
        return currencies
    }

}