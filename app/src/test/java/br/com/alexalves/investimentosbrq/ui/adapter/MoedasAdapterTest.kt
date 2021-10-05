package br.com.alexalves.investimentosbrq.ui.adapter

import android.content.Context
import br.com.alexalves.investimentosbrq.model.Currency
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.math.BigDecimal

class MoedasAdapterTest : TestCase() {

    val context = mock(Context::class.java)

    fun testOnBindViewHolder() {
        val moedas = listOf<Currency>(
            Currency("NOME", BigDecimal.ZERO,BigDecimal.ZERO, 0.0),
            Currency("NOME", BigDecimal.ZERO,BigDecimal.ZERO, 0.0))
        val holder = mock(MoedasAdapter.MoedasViewHolder::class.java)
        val adapter = Mockito.spy(MoedasAdapter(moedas, context, {}))
        Mockito.doNothing().`when`(adapter).vinculaCampos(holder, moedas[1])
        adapter.onBindViewHolder(holder, 1)
        verify(adapter).vinculaCampos(holder, moedas[1])
    }

    fun testGetItemCount() {
        val moedas = listOf<Currency>(
            Currency("NOME", BigDecimal.ZERO,BigDecimal.ZERO, 0.0),
            Currency("NOME", BigDecimal.ZERO,BigDecimal.ZERO, 0.0))
        val adapter = Mockito.spy(MoedasAdapter(moedas, context, {}))
        assertThat(adapter.itemCount, `is`(equalTo(2)))
    }
}