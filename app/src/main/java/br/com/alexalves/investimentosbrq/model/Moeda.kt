package br.com.alexalves.investimentosbrq.model

import java.io.Serializable
import java.math.BigDecimal

class Moeda(
    val name: String,
    val buy: BigDecimal,
    val sell: BigDecimal,
    val variation: Double,
    var abreviacao: String = "indefinido",
    var source: String = "BRL"

): Serializable{

    fun configura(sourceBuscada: String = "indefinido"){
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
        this.source = sourceBuscada
    }

    fun getVariacaoFormatada(): String{
        if (variation!=null){
            val variacaoFormatada = String.format("%.2f",this.variation).replace(".",",")+"%"
            return variacaoFormatada
        } else return "null"
    }

    fun getValorVendaFormatado(): String{
        if (sell!=null){
            if (!source.isBlank()){
                val vendaComDecimais = String.format("%.3f", sell)
                return source+" "+vendaComDecimais.replace(".",",")
            }else return sell.toString().replace(".",",")
        } else return "null"
    }

    fun getValorCompraFormatado(): String{
        if (buy!=null){
            if (!source.isBlank()){
                val compraComDecimais = String.format("%.3f", buy)
                return source+" "+compraComDecimais.replace(".",",")
            } else return buy.toString().replace(".",",")
        } else return "null"
    }
}
