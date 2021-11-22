package br.com.alexalves.models

import java.io.Serializable
import java.math.BigDecimal

data class Currency(
    val name: String,
    val buy: BigDecimal,
    val sell: BigDecimal,
    val variation: Double,
    var abbreviation: String = "undefined",
    var source: String = "BRL"

) : Serializable
