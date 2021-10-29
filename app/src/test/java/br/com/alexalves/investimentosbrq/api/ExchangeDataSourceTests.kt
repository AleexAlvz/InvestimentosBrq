package br.com.alexalves.investimentosbrq.api

import br.com.alexalves.base.database.UserDAO
import br.com.alexalves.base.repository.ExchangeDataSource
import br.com.alexalves.base.repository.InvestimentoServiceAPIWrapper
import br.com.alexalves.api.InvestimentosService
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
    lateinit var service: br.com.alexalves.api.InvestimentosService
    @MockK
    lateinit var exchangeWrapper: InvestimentoServiceAPIWrapper
    lateinit var exchangeDataSource: ExchangeDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        exchangeDataSource = ExchangeDataSource(userDao, service, exchangeWrapper)
    }

    @Test
    fun `When searchUser then returns the correct User`(){

        //Arrange
        val user = br.com.alexalves.models.User(id = 1L)

        coEvery { userDao.searchUser(user.id) } returns user

        //Act
        val userFound = runBlocking { exchangeDataSource.searchUser(user.id) }

        //Assert
        assertEquals(user, userFound)
    }

    @Test
    fun `When searchCurrencies then return currencies`(){
        //Arrange
        val currencies = listOf<br.com.alexalves.models.Currency>(mockk(name="Dollar"), mockk(name="Bitcoin"), mockk(name="Euro"))

        coEvery { service.getService().execute().body() } answers { nothing }
        coEvery { exchangeWrapper.filterCurrencies(null) } returns currencies

        //Act
        val foundCurrencies = runBlocking { exchangeDataSource.searchCurrencies() }

        //Assert
        assertEquals(currencies, foundCurrencies )
    }
}