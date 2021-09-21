package br.com.alexalves.investimentosbrq.model

import java.math.BigDecimal

class Moeda(
    val name: String,
    val buy: BigDecimal,
    val sell: BigDecimal,
    val variation: BigDecimal
)
