package br.com.alexalves.investimentosbrq.model

import android.util.Log
import java.io.Serializable
import java.math.BigDecimal
import java.text.Format

class Moeda(
    val name: String,
    val buy: BigDecimal,
    val sell: BigDecimal,
    val variation: Double,
    var abreviacao: String = "indefinido"
): Serializable{

    fun variacaoComDuasCasasDecimais(): Double{
        val variacaoFormatada = String.format("%.2f",this.variation).toDouble()
        return variacaoFormatada
    }

    fun setAbreviacao(){
        when(this.name){
            "Dollar" -> this.abreviacao = "USD"
            "Euro" -> this.abreviacao = "EUR"
            "Pound Sterling" -> this.abreviacao = "GBP"
            "Argentine Peso" -> this.abreviacao = "ARS"
            "Canadian Dollar" -> this.abreviacao = "CAD"
            "Australian Dollar" -> this.abreviacao = "AUD"
            "Japanese Yen" -> this.abreviacao = "JPY"
            "Renminbi" -> this.abreviacao = "CNY"
            "Bitcoin" -> this.abreviacao = "BTC"
        }
    }
}
