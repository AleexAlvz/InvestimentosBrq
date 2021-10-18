package br.com.alexalves.investimentosbrq.model

import java.io.Serializable
import java.math.BigDecimal

class Currency(
    val name: String,
    val buy: BigDecimal,
    val sell: BigDecimal,
    val variation: Double,
    var abbreviation: String = "undefined",
    var source: String = "BRL"

): Serializable{

    fun setAbbreviationAndSource(source: String = "indefinido"){
        when(this.name){
            "Dollar" -> this.abbreviation = "USD"
            "Euro" -> this.abbreviation = "EUR"
            "Pound Sterling" -> this.abbreviation = "GBP"
            "Argentine Peso" -> this.abbreviation = "ARS"
            "Canadian Dollar" -> this.abbreviation = "CAD"
            "Australian Dollar" -> this.abbreviation = "AUD"
            "Japanese Yen" -> this.abbreviation = "JPY"
            "Renminbi" -> this.abbreviation = "CNY"
            "Bitcoin" -> this.abbreviation = "BTC"
        }
        this.source = source
    }

}
