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
    private lateinit var fieldsObserver: Observer<ScreenExchangeState.InitExchangeFragment>

    @Mock
    private lateinit var screenStateObserver: Observer<ScreenExchangeState>

    val currencyDollar = Currency("DOLLAR", BigDecimal(5), BigDecimal(5), 2.0, "USD")

    @Test()
    fun testInitCambioFragmentSucess() {

        val exchangeRepositoryMock = mock(ExchangeRepository::class.java)

        val user = mock(User::class.java)
        user.balance = BigDecimal(10000000)
        user.usd = BigInteger.ZERO

        val callback = mock(Unit::class.java)

        `when`(exchangeRepositoryMock.searchUser(anyLong(), callback)).thenAnswer{
            val callback = it.getArgument<(result: OperateUser)->Unit>(1)
            callback.invoke(OperateUser.Success(user))
        }

        //Arrange
        val exchangeViewModel = ExchangeViewModel(exchangeRepositoryMock)
        exchangeViewModel.viewScreenState.observeForever( screenStateObserver )

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

class MockExchangeRepositorySucessInSearchUser : ExchangeRepository {

    override fun searchUser(userId: Long, callBackSearchUser: (result: OperateUser) -> Unit) {
        callBackSearchUser.invoke(OperateUser.Success(User()))
    }

    override fun updateUser(user: User, callBackUpdateUser: (result: OperateUser) -> Unit) {
    }

    override fun searchCurrencies(
        whenSucess: (currencies: List<Currency>) -> Unit,
        whenFails: (error: Exception) -> Unit
    ) {
    }
}

