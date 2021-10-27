package br.com.alexalves.investimentosbrq.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alexalves.base.coroutines.AppContextProvider
import br.com.alexalves.base.coroutines.TestContextProvider
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.HomeState
import br.com.alexalves.investimentosbrq.model.exceptions.FailureInFoundCurrenciesException
import br.com.alexalves.investimentosbrq.repository.ExchangeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTest : TestCase(){

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @MockK
    lateinit var exchangeRepository: ExchangeRepository
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        br.com.alexalves.base.coroutines.AppContextProvider.coroutinesContextProviderDelegate =
            br.com.alexalves.base.coroutines.TestContextProvider()
        homeViewModel = HomeViewModel(exchangeRepository)
    }

    @Test
    fun `When findCurrencies is successful then update homeState`(){
        //Arrange
        val moedas = listOf<Currency>(mockk(name="Dollar"), mockk(name="Bitcoin"), mockk(name="Euro"))

        coEvery { exchangeRepository.searchCurrencies() } returns moedas

        //Act
        homeViewModel.findCurrencies()

        //Assert
        val expectedHomeState = HomeState.FoundCurrencies(moedas)
        val actualHomeState = homeViewModel.viewHomeState.value

        assertEquals(expectedHomeState, actualHomeState)
    }

    @Test
    fun `When findCurrencies fails then throws FailureInFoundCurrenciesException`(){
        //Arrange
        coEvery { exchangeRepository.searchCurrencies() } throws FailureInFoundCurrenciesException()

        //Act
        homeViewModel.findCurrencies()

        //Assert

        val expectedHomeState = HomeState.FailureInSearchCurrencies(FailureInFoundCurrenciesException())
        val actualHomeState = homeViewModel.viewHomeState.value as HomeState.FailureInSearchCurrencies

        assertEquals(expectedHomeState.javaClass, actualHomeState.javaClass)
    }

    @Test
    fun `When findCurrencies found a empty list then throws FailureInFoundCurrenciesException with message = List isn't valid`(){
        //Arrange
        val moedas = listOf<Currency>()

        coEvery { exchangeRepository.searchCurrencies() } returns moedas

        //Act
        homeViewModel.findCurrencies()

        //Assert
        val expectedHomeState = HomeState.FailureInSearchCurrencies(FailureInFoundCurrenciesException("List isn't valid"))
        val actualHomeState = homeViewModel.viewHomeState.value as HomeState.FailureInSearchCurrencies

        assertEquals(expectedHomeState.javaClass, actualHomeState.javaClass)
        assertEquals(expectedHomeState.error.message.toString(), actualHomeState.error.message.toString())
    }
}