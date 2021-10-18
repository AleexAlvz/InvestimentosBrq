package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.ServiceInvestimentos

class ExchangeDataSourceWrapper {

    private fun configureReturnOfCurrenciesAPI(response: ServiceInvestimentos): List<Currency> {
        val currencies = response?.results?.currencies
        currencies?.let {
            it.ars.setAbbreviationAndSource(it.source)
            it.aud.setAbbreviationAndSource(it.source)
            it.btc.setAbbreviationAndSource(it.source)
            it.cad.setAbbreviationAndSource(it.source)
            it.cny.setAbbreviationAndSource(it.source)
            it.eur.setAbbreviationAndSource(it.source)
            it.gbp.setAbbreviationAndSource(it.source)
            it.jpy.setAbbreviationAndSource(it.source)
            it.usd.setAbbreviationAndSource(it.source)
        }

        val configuredCurrencies: List<Currency>
        if (currencies!=null){
            configuredCurrencies =  listOf(currencies.ars, currencies.aud, currencies.btc, currencies.cad,
                currencies.cny, currencies.eur, currencies.gbp, currencies.jpy, currencies.usd)
        } else configuredCurrencies =  listOf()

        return configuredCurrencies
    }

     fun filterCurrencies(response: ServiceInvestimentos?): List<Currency> {
         if (response!=null){
             val currencies = arrayListOf<Currency>()
             val configuredCurrencies = configureReturnOfCurrenciesAPI(response)
             for (currency in configuredCurrencies) {
                 if (currency.sell != null && currency.buy != null) {
                     currencies.add(currency)
                 }
             }
             return currencies
         } else return listOf()
    }
}