package br.com.alexalves.investimentosbrq.api

import br.com.alexalves.investimentosbrq.database.UserDAO
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.User
import br.com.alexalves.investimentosbrq.repository.ExchangeDataSource
import br.com.alexalves.investimentosbrq.repository.ExchangeDataSourceWrapper
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExchangeDataSourceTests {

    @MockK
    lateinit var userDao: UserDAO
    @MockK
    lateinit var service: InvestimentosService
    @MockK
    lateinit var exchangeWrapper: ExchangeDataSourceWrapper
    lateinit var exchangeDataSource: ExchangeDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        exchangeDataSource = ExchangeDataSource(userDao, service, exchangeWrapper)
    }

    @Test
    fun `When searchUser then returns the correct User`(){

        //Arrange
        val user = User(id = 1L)

        coEvery { userDao.searchUser(user.id) } returns user

        //Act
        val userFound = runBlocking { exchangeDataSource.searchUser(user.id) }

        //Assert
        assertEquals(user, userFound)
    }

    @Test
    fun `When searchCurrencies then return currencies`(){
        //Arrange
        val currencies = listOf<Currency>(mockk(name="Dollar"), mockk(name="Bitcoin"), mockk(name="Euro"))

        coEvery { service.getService().execute().body() } answers { nothing }
        coEvery { exchangeWrapper.filterCurrencies(null) } returns currencies

        //Act
        val foundCurrencies = runBlocking { exchangeDataSource.searchCurrencies() }

        //Assert
        assertEquals(currencies, foundCurrencies )
    }
}