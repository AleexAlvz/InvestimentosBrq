package br.com.alexalves.investimentosbrq.model

import br.com.alexalves.utils.CurrencyUtils
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal

class CurrencyTests {

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaDollar() {
        val moedaDollar =
            br.com.alexalves.models.Currency("Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        CurrencyUtils.setAbbreviationAndSource(moedaDollar)
        assertThat("USD", `is`(equalTo(moedaDollar.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaEuro() {
        val moedaEuro =
            br.com.alexalves.models.Currency("Euro", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        CurrencyUtils.setAbbreviationAndSource(moedaEuro)
        assertThat("EUR", `is`(equalTo(moedaEuro.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaPoundSterling() {
        val moedaPoundSterling = br.com.alexalves.models.Currency(
            "Pound Sterling",
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            0.0
        )
        CurrencyUtils.setAbbreviationAndSource(moedaPoundSterling)
        assertThat("GBP", `is`(equalTo(moedaPoundSterling.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaArgentinePeso() {
        val moedaArgentinePeso = br.com.alexalves.models.Currency(
            "Argentine Peso",
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            0.0
        )
        CurrencyUtils.setAbbreviationAndSource(moedaArgentinePeso)
        assertThat("ARS", `is`(equalTo(moedaArgentinePeso.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaCanadianDollar() {
        val moedaCanadianDollar = br.com.alexalves.models.Currency(
            "Canadian Dollar",
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            0.0
        )
        CurrencyUtils.setAbbreviationAndSource(moedaCanadianDollar)
        assertThat("CAD", `is`(equalTo(moedaCanadianDollar.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaAustralianDollar() {
        val moedaAustralianDollar =
            br.com.alexalves.models.Currency(
                "Australian Dollar",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0.0
            )
        CurrencyUtils.setAbbreviationAndSource(moedaAustralianDollar)
        assertThat("AUD", `is`(equalTo(moedaAustralianDollar.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaJapaneseYen() {
        val moedaJapaneseYen =
            br.com.alexalves.models.Currency("Japanese Yen", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        CurrencyUtils.setAbbreviationAndSource(moedaJapaneseYen)
        assertThat("JPY", `is`(equalTo(moedaJapaneseYen.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaRenminbi() {
        val moedaRenminbi =
            br.com.alexalves.models.Currency("Renminbi", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        CurrencyUtils.setAbbreviationAndSource(moedaRenminbi)
        assertThat("CNY", `is`(equalTo(moedaRenminbi.abbreviation)))
    }

    @Test
    fun deve_configurarAbreviacao_quandoConfiguraMoedaBitcoin() {
        val moedaBitcoin =
            br.com.alexalves.models.Currency("Bitcoin", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        CurrencyUtils.setAbbreviationAndSource(moedaBitcoin)
        assertThat("BTC", `is`(equalTo(moedaBitcoin.abbreviation)))
    }

    @Test
    fun testGetVariacaoFormatada(){
        val currency =
            br.com.alexalves.models.Currency("TESTE", BigDecimal.ZERO, BigDecimal.ZERO, 25.75)
        val formatted = CurrencyUtils.getFormattedVariation(currency.variation)
        assertThat(formatted, `is`(equalTo("25,75%")))
    }

    @Test
    fun testGetValorVendaFormatadoQuandoValorZero(){
        val currency =
            br.com.alexalves.models.Currency("TESTE", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        val formatted = CurrencyUtils.getFormattedSaleValue(currency)
        assertThat(formatted, `is`(equalTo("BRL 0,000")))
    }

    @Test
    fun testGetValorVendaFormatadoQuandoValorMaiorQueZero(){
        val currency =
            br.com.alexalves.models.Currency("TESTE", BigDecimal.ZERO, BigDecimal(3.751), 0.0)
        val formatted = CurrencyUtils.getFormattedSaleValue(currency)
        assertThat(formatted, `is`(equalTo("BRL 3,751")))
    }

    @Test
    fun testGetValorCompraFormatadoQuandoValorZero(){
        val moeda = br.com.alexalves.models.Currency("TESTE", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        val formatted = CurrencyUtils.getFormattedPurchaseValue(moeda)
        assertThat(formatted, `is`(equalTo("BRL 0,000")))
    }

    @Test
    fun testGetValorCompraFormatadoQuandoValorMaiorQueZero(){
        val currency =
            br.com.alexalves.models.Currency("TESTE", BigDecimal(3.751), BigDecimal.ZERO, 0.0)
        val formatted = CurrencyUtils.getFormattedPurchaseValue(currency)
        assertThat(formatted, `is`(equalTo("BRL 3,751")))
    }

}