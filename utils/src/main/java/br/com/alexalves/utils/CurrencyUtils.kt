package br.com.alexalves.utils

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.alexalves.models.Currency
import br.com.alexalves.models.User
import br.com.alexalves.models.consts.AbbreviationCurrenciesConsts
import java.math.BigDecimal
import java.math.BigInteger

object CurrencyUtils {

    fun getFormattedSaleValue(currency: Currency): String {
        val vendaComDecimais = String.format("%.3f", currency.sell)
        return "R$ " + vendaComDecimais.replace(".", ",")
    }

    fun getFormattedValue_ToBRLCurrency(balance: BigDecimal): String {
        return "R$ " + String.format("%.2f", balance).replace(".", ",")
    }

    fun getFormattedPurchaseValue(currency: Currency): String {
        val compraComDecimais = String.format("%.3f", currency.buy)
        return "R$ " + compraComDecimais.replace(".", ",")
    }

    fun getCurrencyColor(variation: Double, context: Context): Int {
        val color = if (variation > 0) {
            ContextCompat.getColor(context, R.color.green_positive)
        } else if (variation < 0) {
            ContextCompat.getColor(context, R.color.red_negative)
        } else ContextCompat.getColor(context, R.color.white)
        return color
    }

    fun getFormattedVariation(variation: Double): String {
        val variacao = String.format("%.2f", variation).replace(".", ",") + "%"
        return variacao
    }

    fun setAbbreviationAndSource(currency: Currency, source: String = "indefinido"){
        when(currency.name){
            "Dollar" -> currency.abbreviation = "USD"
            "Euro" -> currency.abbreviation = "EUR"
            "Pound Sterling" -> currency.abbreviation = "GBP"
            "Argentine Peso" -> currency.abbreviation = "ARS"
            "Canadian Dollar" -> currency.abbreviation = "CAD"
            "Australian Dollar" -> currency.abbreviation = "AUD"
            "Japanese Yen" -> currency.abbreviation = "JPY"
            "Renminbi" -> currency.abbreviation = "CNY"
            "Bitcoin" -> currency.abbreviation = "BTC"
        }
        currency.source = source
    }

    fun filterCurrency(currency: Currency, user: User): BigInteger {
        return when (currency.abbreviation) {
            AbbreviationCurrenciesConsts.USD -> user.usd
            AbbreviationCurrenciesConsts.EUR -> user.eur
            AbbreviationCurrenciesConsts.GBP -> user.gbp
            AbbreviationCurrenciesConsts.ARS -> user.ars
            AbbreviationCurrenciesConsts.CAD -> user.cad
            AbbreviationCurrenciesConsts.AUD -> user.aud
            AbbreviationCurrenciesConsts.JPY -> user.jpy
            AbbreviationCurrenciesConsts.CNY -> user.cny
            AbbreviationCurrenciesConsts.BTC -> user.btc
            else -> throw Exception(AbbreviationCurrenciesConsts.CURRENCY_NOT_FOUND)
        }
    }
}