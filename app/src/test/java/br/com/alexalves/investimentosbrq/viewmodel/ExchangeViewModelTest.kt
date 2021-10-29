package br.com.alexalves.investimentosbrq.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alexalves.feature_exchange.ui.viewmodels.ExchangeViewModel
import br.com.alexalves.base.repository.ExchangeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal
import java.math.BigInteger

@RunWith(JUnit4::class)
class ExchangeViewModelTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @MockK
    lateinit var exchangeRepository: ExchangeRepository
    lateinit var exchangeViewModel: ExchangeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        br.com.alexalves.base.coroutines.AppContextProvider.coroutinesContextProviderDelegate =
            br.com.alexalves.base.coroutines.TestContextProvider()
        exchangeViewModel = ExchangeViewModel(exchangeRepository)
    }

    //Tests InitExchangeFragment

    @Test
    fun `When initExchangeFragment is successful then update screenState`() {
        //Arrange
        val currency = br.com.alexalves.models.Currency(
            "Dollar",
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            0.10,
            "USD"
        )
        val user = br.com.alexalves.models.User()

        coEvery { exchangeRepository.searchUser(user.id) } returns user

        //Act
        exchangeViewModel.initCambioFragment(currency, user.id)

        //Assert
        val expectedScreenState = br.com.alexalves.models.ScreenExchangeState.InitExchangeFragment(currency, user.balance, user.usd)
        val actualScreenState = exchangeViewModel.viewScreenState.value

        assertEquals(expectedScreenState, actualScreenState)
    }

    @Test
    fun `When initExchangeFragment fails in search user Then catch userNotFoundException in screenState`() {
        //Arrange
        val currency = br.com.alexalves.models.Currency(
            "Dollar",
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            0.10,
            "USD"
        )
        val user = br.com.alexalves.models.User()
        val userNotFoundException = br.com.alexalves.models.exceptions.UserNotFoundException()

        coEvery { exchangeRepository.searchUser(user.id) } throws userNotFoundException

        //Act
        exchangeViewModel.initCambioFragment(currency, user.id)

        //Assert
        val actualScreenState = exchangeViewModel.viewScreenState.value as br.com.alexalves.models.ScreenExchangeState.FailureInInitExchangeFragment

        assertEquals(userNotFoundException.javaClass, actualScreenState.error.javaClass)
    }

    //Tests Purchase

    @Test
    fun `When purchaseCurrency successful with balance greater than finalValue Then update businessState`() {
        //Arrange
        val currency =
            br.com.alexalves.models.Currency("Dollar", BigDecimal(5), BigDecimal.ZERO, 0.10, "USD")
        val quantity = BigInteger("10")
        val finalValue = quantity.toBigDecimal()*currency.buy
        val user = br.com.alexalves.models.User()

        coEvery { exchangeRepository.searchUser(user.id) } returns user

        //Act
        exchangeViewModel.purchaseCurrency(currency, quantity, user.id)

        //Assert
        val expectedBusinessState = br.com.alexalves.models.BusinessExchangeState.Sucess(quantity, finalValue)

        val actualBusinessState = exchangeViewModel.viewBusinessExchangeState.value as br.com.alexalves.models.BusinessExchangeState.Sucess

        assertEquals(expectedBusinessState, actualBusinessState)
    }

    @Test
    fun `When purchaseCurrency successful with balance equals finalValue Then update businessState`() {
        //Arrange
        val currency =
            br.com.alexalves.models.Currency("Dollar", BigDecimal(5), BigDecimal.ZERO, 0.10, "USD")
        val quantity = BigInteger("10")
        val finalValue = quantity.toBigDecimal()*currency.buy
        val user = br.com.alexalves.models.User(balance = BigDecimal(finalValue.toString()))

        coEvery { exchangeRepository.searchUser(user.id) } returns user

        //Act
        exchangeViewModel.purchaseCurrency(currency, quantity, user.id)

        //Assert

        val expectedBusinessState = br.com.alexalves.models.BusinessExchangeState.Sucess(quantity, finalValue)

        val actualBusinessState = exchangeViewModel.viewBusinessExchangeState.value as br.com.alexalves.models.BusinessExchangeState.Sucess

        assertEquals(expectedBusinessState, actualBusinessState)
    }

    @Test
    fun `When purchaseCurrency fails in search user Then catch UserNotFoundException in businessState`() {
        //Arrange
        val currency =
            br.com.alexalves.models.Currency("Dollar", BigDecimal(5), BigDecimal.ZERO, 0.10, "USD")
        val quantity = BigInteger("10")
        val user = br.com.alexalves.models.User()

        coEvery { exchangeRepository.searchUser(user.id) } throws br.com.alexalves.models.exceptions.UserNotFoundException()

        //Act
        exchangeViewModel.purchaseCurrency(currency, quantity, user.id)

        //Assert

        val actualBusinessState = exchangeViewModel.viewBusinessExchangeState.value as br.com.alexalves.models.BusinessExchangeState.Failure

        assertEquals(br.com.alexalves.models.exceptions.UserNotFoundException().javaClass, actualBusinessState.error.javaClass)
    }

    @Test
    fun `When purchaseCurrency not approval purchase Then catch UserNotFoundException in businessState`() {
        //Arrange
        val currency =
            br.com.alexalves.models.Currency("Dollar", BigDecimal(5), BigDecimal.ZERO, 0.10, "USD")
        val quantity = BigInteger("10")
        val user = br.com.alexalves.models.User(balance = BigDecimal.ZERO)

        coEvery { exchangeRepository.searchUser(user.id) } returns user

        //Act
        exchangeViewModel.purchaseCurrency(currency, quantity, user.id)

        //Assert
        val actualBusinessState = exchangeViewModel.viewBusinessExchangeState.value as br.com.alexalves.models.BusinessExchangeState.Failure
        assertEquals(br.com.alexalves.models.exceptions.PurchaseNotApprovalException().javaClass, actualBusinessState.error.javaClass)
    }

    //Tests Sale

    @Test
    fun `When saleCurrency successful with amount request less than amountCurrency of user Then update businessState`() {
        //Arrange
        val currency =
            br.com.alexalves.models.Currency("Dollar", BigDecimal.ZERO, BigDecimal(5), 0.10, "USD")
        val amountRequest = BigInteger("10")
        val finalValue = amountRequest.toBigDecimal()*currency.sell
        val user = br.com.alexalves.models.User(usd = BigInteger("20"))

        coEvery { exchangeRepository.searchUser(user.id) } returns user

        //Act
        exchangeViewModel.saleCurrency(currency, amountRequest, user.id)

        //Assert
        val expectedBusinessState = br.com.alexalves.models.BusinessExchangeState.SucessSale(amountRequest, finalValue)

        val actualBusinessState = exchangeViewModel.viewBusinessExchangeState.value as br.com.alexalves.models.BusinessExchangeState.SucessSale

        assertEquals(expectedBusinessState, actualBusinessState)
    }

    @Test
    fun `When saleCurrency successful with amount request equals amountCurrency of user Then update businessState`() {
        //Arrange
        val currency =
            br.com.alexalves.models.Currency("Dollar", BigDecimal.ZERO, BigDecimal(5), 0.10, "USD")
        val amountRequest = BigInteger("10")
        val finalValue = amountRequest.toBigDecimal()*currency.sell
        val user = br.com.alexalves.models.User(usd = BigInteger("10"))

        coEvery { exchangeRepository.searchUser(user.id) } returns user

        //Act
        exchangeViewModel.saleCurrency(currency, amountRequest, user.id)

        //Assert
        val expectedBusinessState = br.com.alexalves.models.BusinessExchangeState.SucessSale(amountRequest, finalValue)

        val actualBusinessState = exchangeViewModel.viewBusinessExchangeState.value as br.com.alexalves.models.BusinessExchangeState.SucessSale

        assertEquals(expectedBusinessState, actualBusinessState)
    }

    @Test
    fun `When saleCurrency fails in search user Then catch UserNotFoundException in businessState`() {
        //Arrange
        val currency =
            br.com.alexalves.models.Currency("Dollar", BigDecimal(5), BigDecimal.ZERO, 0.10, "USD")
        val quantity = BigInteger("10")
        val user = br.com.alexalves.models.User()

        coEvery { exchangeRepository.searchUser(user.id) } throws br.com.alexalves.models.exceptions.UserNotFoundException()

        //Act
        exchangeViewModel.saleCurrency(currency, quantity, user.id)

        //Assert

        val actualBusinessState = exchangeViewModel.viewBusinessExchangeState.value as br.com.alexalves.models.BusinessExchangeState.FailureSale

        assertEquals(br.com.alexalves.models.exceptions.UserNotFoundException().javaClass, actualBusinessState.error.javaClass)
    }

    @Test
    fun `When saleCurrency not approval purchase Then catch UserNotFoundException in businessState`() {
        //Arrange
        val currency =
            br.com.alexalves.models.Currency("Dollar", BigDecimal(5), BigDecimal.ZERO, 0.10, "USD")
        val quantity = BigInteger("10")
        val user = br.com.alexalves.models.User(usd = BigInteger.ZERO)

        coEvery { exchangeRepository.searchUser(user.id) } returns user

        //Act
        exchangeViewModel.saleCurrency(currency, quantity, user.id)

        //Assert
        val actualBusinessState = exchangeViewModel.viewBusinessExchangeState.value as br.com.alexalves.models.BusinessExchangeState.FailureSale
        assertEquals(br.com.alexalves.models.exceptions.SaleNotApprovalException().javaClass, actualBusinessState.error.javaClass)
    }

}


