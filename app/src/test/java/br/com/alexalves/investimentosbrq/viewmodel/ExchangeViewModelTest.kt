package br.com.alexalves.investimentosbrq.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.OperateUser
import br.com.alexalves.investimentosbrq.model.ScreenExchangeState
import br.com.alexalves.investimentosbrq.model.User
import br.com.alexalves.investimentosbrq.repository.ExchangeDataSource
import br.com.alexalves.investimentosbrq.repository.ExchangeRepository
import junit.framework.TestCase
import kotlinx.coroutines.flow.callbackFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ExchangeViewModelTest : TestCase() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var screenStateObserver: Observer<ScreenExchangeState>

    @Test
    fun testInitCambioFragmentSucess() {

        //Arrange
        val exchangeViewModel = mock(ExchangeViewModel::class.java)

        val currencyDollar = Currency("DOLLAR", BigDecimal(5), BigDecimal(5), 2.0, "USD")
        val user = User()
        user.id = 1L

        `when`(exchangeViewModel.initCambioFragment(currencyDollar, 1L)).thenAnswer{
            exchangeViewModel.screenExchangeStateToInitFragment(currencyDollar, user.balance, user.usd)
        }

        exchangeViewModel.viewScreenState.observeForever(screenStateObserver)

        //Act
        exchangeViewModel.initCambioFragment(currencyDollar, 1L)

        //Assert
        verify(screenStateObserver).onChanged(
            ScreenExchangeState.InitExchangeFragment(
                currencyDollar,
                BigDecimal(10000000),
                BigInteger.ZERO)
        )
    }

    @Test
    fun testInitCambioFragmentCurrencyNotFound(){
    }

}

