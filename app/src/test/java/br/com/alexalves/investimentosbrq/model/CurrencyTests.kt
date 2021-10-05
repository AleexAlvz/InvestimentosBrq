package br.com.alexalves.investimentosbrq.model

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal

class CurrencyTests {

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaDollar() {
        val moedaDollar = Currency("Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaDollar.setAbbreviationAndSource()
        assertThat("USD", `is`(equalTo(moedaDollar.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaEuro() {
        val moedaEuro = Currency("Euro", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaEuro.setAbbreviationAndSource()
        assertThat("EUR", `is`(equalTo(moedaEuro.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaPoundSterling() {
        val moedaPoundSterling = Currency("Pound Sterling", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaPoundSterling.setAbbreviationAndSource()
        assertThat("GBP", `is`(equalTo(moedaPoundSterling.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaArgentinePeso() {
        val moedaArgentinePeso = Currency("Argentine Peso", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaArgentinePeso.setAbbreviationAndSource()
        assertThat("ARS", `is`(equalTo(moedaArgentinePeso.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaCanadianDollar() {
        val moedaCanadianDollar = Currency("Canadian Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaCanadianDollar.setAbbreviationAndSource()
        assertThat("CAD", `is`(equalTo(moedaCanadianDollar.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaAustralianDollar() {
        val moedaAustralianDollar =
            Currency("Australian Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaAustralianDollar.setAbbreviationAndSource()
        assertThat("AUD", `is`(equalTo(moedaAustralianDollar.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaJapaneseYen() {
        val moedaJapaneseYen = Currency("Japanese Yen", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaJapaneseYen.setAbbreviationAndSource()
        assertThat("JPY", `is`(equalTo(moedaJapaneseYen.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaRenminbi() {
        val moedaRenminbi = Currency("Renminbi", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaRenminbi.setAbbreviationAndSource()
        assertThat("CNY", `is`(equalTo(moedaRenminbi.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaBitcoin() {
        val moedaBitcoin = Currency("Bitcoin", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaBitcoin.setAbbreviationAndSource()
        assertThat("BTC", `is`(equalTo(moedaBitcoin.abbreviation)))
    }

    @Test
    fun testGetVariacaoFormatada(){
        val moeda = Currency("TESTE", BigDecimal.ZERO, BigDecimal.ZERO, 25.75)
        val formatacao = moeda.getVariacaoFormatada()
        assertThat(formatacao, `is`(equalTo("25,75%")))
    }

    @Test
    fun testGetValorVendaFormatadoQuandoValorZero(){
        val moeda = Currency("TESTE", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        val formatacao = moeda.getValorVendaFormatado()
        assertThat(formatacao, `is`(equalTo("BRL 0,000")))
    }

    @Test
    fun testGetValorVendaFormatadoQuandoValorMaiorQueZero(){

    }

    @Test
    fun testGetValorCompraFormatadoQuandoValorZero(){
        val moeda = Currency("TESTE", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        val formatacao = moeda.getValorCompraFormatado()
        assertThat(formatacao, `is`(equalTo("BRL 0,000")))
    }

    @Test
    fun testGetValorCompraFormatadoQuandoValorMaiorQueZero(){
        val moeda = Currency("TESTE", BigDecimal(3.751), BigDecimal.ZERO, 0.0)
        val formatacao = moeda.getValorCompraFormatado()
        assertThat(formatacao, `is`(equalTo("BRL 3,751")))
    }

}