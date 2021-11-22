package br.com.alexalves.models

import com.google.gson.annotations.SerializedName

class Currencies(
    val source: String,
    @SerializedName("USD")
    val usd: Currency,
    @SerializedName("EUR")
    val eur: Currency,
    @SerializedName("GBP")
    val gbp: Currency,
    @SerializedName("ARS")
    val ars: Currency,
    @SerializedName("CAD")
    val cad: Currency,
    @SerializedName("AUD")
    val aud: Currency,
    @SerializedName("JPY")
    val jpy: Currency,
    @SerializedName("CNY")
    val cny: Currency,
    @SerializedName("BTC")
    val btc: Currency
)
