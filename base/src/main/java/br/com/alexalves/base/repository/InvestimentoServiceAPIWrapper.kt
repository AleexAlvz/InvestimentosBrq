package br.com.alexalves.base.repository

import br.com.alexalves.models.Currency
import br.com.alexalves.models.ServiceInvestimentos
import br.com.alexalves.utils.CurrencyUtils

class InvestimentoServiceAPIWrapper {

    private fun configureReturnOfCurrenciesAPI(response: ServiceInvestimentos): List<Currency> {
        val currencies = response?.results?.currencies
        currencies?.let {
            CurrencyUtils.setAbbreviationAndSource(it.ars, it.source)
            CurrencyUtils.setAbbreviationAndSource(it.aud, it.source)
            CurrencyUtils.setAbbreviationAndSource(it.btc, it.source)
            CurrencyUtils.setAbbreviationAndSource(it.cad, it.source)
            CurrencyUtils.setAbbreviationAndSource(it.cny, it.source)
            CurrencyUtils.setAbbreviationAndSource(it.eur, it.source)
            CurrencyUtils.setAbbreviationAndSource(it.gbp, it.source)
            CurrencyUtils.setAbbreviationAndSource(it.jpy, it.source)
            CurrencyUtils.setAbbreviationAndSource(it.usd, it.source)
        }

        val configuredCurrencies: List<Currency>
        if (currencies != null) {
            configuredCurrencies = listOf(
                currencies.ars, currencies.aud, currencies.btc, currencies.cad,
                currencies.cny, currencies.eur, currencies.gbp, currencies.jpy, currencies.usd
            )
        } else configuredCurrencies = listOf()

        return configuredCurrencies
    }

    fun filterCurrencies(response: ServiceInvestimentos?): List<Currency> {
        if (response != null) {
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