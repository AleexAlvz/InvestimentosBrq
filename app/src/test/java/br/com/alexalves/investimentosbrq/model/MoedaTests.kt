package br.com.alexalves.investimentosbrq.model

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.experimental.theories.suppliers.TestedOn
import java.math.BigDecimal

class MoedaTests {

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaDollar() {
        val moedaDollar = Moeda("Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaDollar.configura()
        assertThat("USD", `is`(equalTo(moedaDollar.abreviacao)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaEuro() {
        val moedaEuro = Moeda("Euro", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaEuro.configura()
        assertThat("EUR", `is`(equalTo(moedaEuro.abreviacao)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaPoundSterling() {
        val moedaPoundSterling = Moeda("Pound Sterling", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaPoundSterling.configura()
        assertThat("GBP", `is`(equalTo(moedaPoundSterling.abreviacao)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaArgentinePeso() {
        val moedaArgentinePeso = Moeda("Argentine Peso", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaArgentinePeso.configura()
        assertThat("ARS", `is`(equalTo(moedaArgentinePeso.abreviacao)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaCanadianDollar() {
        val moedaCanadianDollar = Moeda("Canadian Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaCanadianDollar.configura()
        assertThat("CAD", `is`(equalTo(moedaCanadianDollar.abreviacao)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaAustralianDollar() {
        val moedaAustralianDollar =
            Moeda("Australian Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaAustralianDollar.configura()
        assertThat("AUD", `is`(equalTo(moedaAustralianDollar.abreviacao)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaJapaneseYen() {
        val moedaJapaneseYen = Moeda("Japanese Yen", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaJapaneseYen.configura()
        assertThat("JPY", `is`(equalTo(moedaJapaneseYen.abreviacao)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaRenminbi() {
        val moedaRenminbi = Moeda("Renminbi", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaRenminbi.configura()
        assertThat("CNY", `is`(equalTo(moedaRenminbi.abreviacao)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaBitcoin() {
        val moedaBitcoin = Moeda("Bitcoin", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaBitcoin.configura()
        assertThat("BTC", `is`(equalTo(moedaBitcoin.abreviacao)))
    }

    @Test
    fun testGetVariacaoFormatada(){
        val moeda = Moeda("TESTE", BigDecimal.ZERO, BigDecimal.ZERO, 25.75)
        val formatacao = moeda.getVariacaoFormatada()
        assertThat(formatacao, `is`(equalTo("25,75%")))
    }

    @Test
    fun testGetValorVendaFormatadoQuandoValorZero(){
        val moeda = Moeda("TESTE", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        val formatacao = moeda.getValorVendaFormatado()
        assertThat(formatacao, `is`(equalTo("BRL 0,000")))
    }

    @Test
    fun testGetValorVendaFormatadoQuandoValorMaiorQueZero(){

    }

    @Test
    fun testGetValorCompraFormatadoQuandoValorZero(){
        val moeda = Moeda("TESTE", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        val formatacao = moeda.getValorCompraFormatado()
        assertThat(formatacao, `is`(equalTo("BRL 0,000")))
    }

    @Test
    fun testGetValorCompraFormatadoQuandoValorMaiorQueZero(){
        val moeda = Moeda("TESTE", BigDecimal(3.751), BigDecimal.ZERO, 0.0)
        val formatacao = moeda.getValorCompraFormatado()
        assertThat(formatacao, `is`(equalTo("BRL 3,751")))
    }

}