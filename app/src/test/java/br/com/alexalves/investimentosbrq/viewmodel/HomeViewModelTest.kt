package br.com.alexalves.investimentosbrq.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alexalves.base.coroutines.AppContextProvider
import br.com.alexalves.base.coroutines.TestContextProvider
import br.com.alexalves.base.repository.HomeRepository
import br.com.alexalves.models.Currency
import br.com.alexalves.models.HomeState.FailureInSearchCurrencies
import br.com.alexalves.models.HomeState.FoundCurrencies
import br.com.alexalves.models.exceptions.FailureInFoundCurrenciesException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @MockK
    lateinit var homeRepository: HomeRepository
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        AppContextProvider.coroutinesContextProviderDelegate =
            TestContextProvider()
        homeViewModel = HomeViewModel(homeRepository)
    }

    @Test
    fun `When findCurrencies is successful then update homeState`() {
        //Arrange
        val moedas =
            listOf<Currency>(mockk(name = "Dollar"), mockk(name = "Bitcoin"), mockk(name = "Euro"))

        coEvery { homeRepository.searchCurrencies() } returns moedas

        //Act
        homeViewModel.findCurrencies()

        //Assert
        val expectedHomeState = FoundCurrencies(moedas)
        val actualHomeState = homeViewModel.viewHomeState.value

        assertEquals(expectedHomeState, actualHomeState)
    }

    @Test
    fun `When findCurrencies fails then throws FailureInFoundCurrenciesException`() {
        //Arrange
        coEvery { homeRepository.searchCurrencies() } throws FailureInFoundCurrenciesException()

        //Act
        homeViewModel.findCurrencies()

        //Assert

        val expectedHomeState = FailureInSearchCurrencies(FailureInFoundCurrenciesException())
        val actualHomeState = homeViewModel.viewHomeState.value as FailureInSearchCurrencies

        assertEquals(expectedHomeState.javaClass, actualHomeState.javaClass)
    }

    @Test
    fun `When findCurrencies found a empty list then throws FailureInFoundCurrenciesException with message = List isn't valid`() {
        //Arrange
        val moedas = listOf<Currency>()

        coEvery { homeRepository.searchCurrencies() } returns moedas

        //Act
        homeViewModel.findCurrencies()

        //Assert
        val expectedHomeState = FailureInSearchCurrencies(
            FailureInFoundCurrenciesException(
                "List isn't valid"
            )
        )
        val actualHomeState = homeViewModel.viewHomeState.value as FailureInSearchCurrencies

        assertEquals(expectedHomeState.javaClass, actualHomeState.javaClass)
        assertEquals(
            expectedHomeState.error.message.toString(),
            actualHomeState.error.message.toString()
        )
    }
}