package br.com.alexalves.investimentosbrq.model

import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal

class MoedaTests {

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaDollar() {
        val moedaDollar = Moeda("Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaDollar.configura()
        assertThat(moedaDollar.abreviacao, `is`(equalTo("USD")))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaEuro() {
        val moedaEuro = Moeda("Euro", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaEuro.configura()
        assertThat(moedaEuro.abreviacao, `is`(equalTo("EUR")))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaPoundSterling() {
        val moedaPoundSterling = Moeda("Pound Sterling", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaPoundSterling.configura()
        assertThat(moedaPoundSterling.abreviacao, `is`(equalTo("GBP")))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaArgentinePeso() {
        val moedaArgentinePeso = Moeda("Argentine Peso", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaArgentinePeso.configura()
        assertThat(moedaArgentinePeso.abreviacao, `is`(equalTo("ARS")))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaCanadianDollar() {
        val moedaCanadianDollar = Moeda("Canadian Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaCanadianDollar.configura()
        assertThat(moedaCanadianDollar.abreviacao, `is`(equalTo("CAD")))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaAustralianDollar() {
        val moedaAustralianDollar =
            Moeda("Australian Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaAustralianDollar.configura()
        assertThat(moedaAustralianDollar.abreviacao, `is`(equalTo("AUD")))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaJapaneseYen() {
        val moedaJapaneseYen = Moeda("Japanese Yen", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaJapaneseYen.configura()
        assertThat(moedaJapaneseYen.abreviacao, `is`(equalTo("JPY")))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaRenminbi() {
        val moedaRenminbi = Moeda("Renminbi", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaRenminbi.configura()
        assertThat(moedaRenminbi.abreviacao, `is`(equalTo("CNY")))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaBitcoin() {
        val moedaBitcoin = Moeda("Bitcoin", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        moedaBitcoin.configura()
        assertThat(moedaBitcoin.abreviacao, `is`(equalTo("BTC")))
    }

}