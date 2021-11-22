package br.com.alexalves.investimentosbrq.ui.adapter

import br.com.alexalves.models.Currency
import io.mockk.MockKAnnotations
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal

@RunWith(JUnit4::class)
class CurrencyAdapterTest {

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `When instance CurrencyAdapter and call getItemCount then return the correct count`(){
        //Arrange
        val currencies = listOf(
            Currency("NOME", BigDecimal.ZERO, BigDecimal.ZERO, 0.0),
            Currency("NOME", BigDecimal.ZERO, BigDecimal.ZERO, 0.0)
        )
        val adapter = CurrencyAdapter(currencies, mockk(), mockk())

        //Act
        val count = adapter.itemCount

        //Assert
        assertEquals(currencies.size, count)
    }
}