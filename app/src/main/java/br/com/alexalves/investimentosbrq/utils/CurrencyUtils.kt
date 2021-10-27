package br.com.alexalves.investimentosbrq.utils

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.alexalves.investimentosbrq.consts.AbbreviationCurrenciesConsts
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.User
import java.math.BigDecimal
import java.math.BigInteger

class CurrencyUtils {

    fun filterCurrency(currency: Currency, user: User): BigInteger {
        return when (currency.abbreviation) {
            AbbreviationCurrenciesConsts().USD -> user.usd
            AbbreviationCurrenciesConsts().EUR -> user.eur
            AbbreviationCurrenciesConsts().GBP -> user.gbp
            AbbreviationCurrenciesConsts().ARS -> user.ars
            AbbreviationCurrenciesConsts().CAD -> user.cad
            AbbreviationCurrenciesConsts().AUD -> user.aud
            AbbreviationCurrenciesConsts().JPY -> user.jpy
            AbbreviationCurrenciesConsts().CNY -> user.cny
            AbbreviationCurrenciesConsts().BTC -> user.btc
            else -> throw Exception(AbbreviationCurrenciesConsts().CURRENCY_NOT_FOUND)
        }
    }

    fun getFormattedVariation(variation: Double): String {
        val variacaoString = variation.toString().substring(0,3)
        val variacaoFormatada = variacaoString.replace(".", ",") + "%"
        return variacaoFormatada
    }

    fun getFormattedSaleValue(currency: Currency): String {
        val vendaComDecimais = String.format("%.3f", currency.sell)
        return currency.source + " " + vendaComDecimais.replace(".", ",")
    }

    fun getFormattedValue_ToBRLCurrency(balance: BigDecimal): String {
        return "R$ " + String.format("%.2f", balance).replace(".", ",")
    }

    fun getFormattedPurchaseValue(currency: Currency): String {
        val compraComDecimais = String.format("%.3f", currency.buy)
        return currency.source + " " + compraComDecimais.replace(".", ",")
    }

    fun getCurrencyColor(variation: Double, context: Context): Int {
        val color = if (variation > 0) {
            ContextCompat.getColor(context, R.color.green_positive)
        } else if (variation < 0) {
            ContextCompat.getColor(context, R.color.red_negative)
        } else ContextCompat.getColor(context, R.color.white)
        return color
    }
}