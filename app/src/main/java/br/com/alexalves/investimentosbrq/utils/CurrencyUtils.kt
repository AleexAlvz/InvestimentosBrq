package br.com.alexalves.investimentosbrq.utils

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.User
import java.math.BigInteger

class CurrencyUtils {

    fun filterCurrency(currency: Currency, user: User): BigInteger {
        return when (currency.abbreviation) {
            "USD" -> user.usd
            "EUR" -> user.eur
            "GBP" -> user.gbp
            "ARS" -> user.ars
            "CAD" -> user.cad
            "AUD" -> user.aud
            "JPY" -> user.jpy
            "CNY" -> user.cny
            "BTC" -> user.btc
            else -> throw Exception("Moeda não encontrada")
        }
    }

    fun getVariacaoFormatada(variation: Double): String {
        val variacaoFormatada = String.format("%.2f", variation).replace(".", ",") + "%"
        return variacaoFormatada
    }

    fun getValorVendaFormatado(currency: Currency): String {
        val vendaComDecimais = String.format("%.3f", currency.sell)
        return currency.source + " " + vendaComDecimais.replace(".", ",")
    }

    fun getValorCompraFormatado(currency: Currency): String {
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