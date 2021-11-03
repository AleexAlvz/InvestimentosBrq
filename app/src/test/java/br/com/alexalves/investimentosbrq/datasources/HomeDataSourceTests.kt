package br.com.alexalves.investimentosbrq.datasources

import br.com.alexalves.base.database.UserDAO
import br.com.alexalves.base.repository.HomeDataSource
import br.com.alexalves.base.repository.InvestimentoServiceAPIWrapper
import br.com.alexalves.base.service.InvestimentosService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeDataSourceTests {

    @MockK
    lateinit var userDao: UserDAO
    @MockK
    lateinit var service: InvestimentosService
    @MockK
    lateinit var apiWrapper: InvestimentoServiceAPIWrapper
    lateinit var homeDataSource: HomeDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        homeDataSource = HomeDataSource(userDao, apiWrapper, service)
    }

    @Test
    fun `When searchCurrencies then return currencies`(){
        //Arrange
        val currencies = listOf<br.com.alexalves.models.Currency>(mockk(name="Dollar"), mockk(name="Bitcoin"), mockk(name="Euro"))

        coEvery { service.getService().execute().body() } answers { nothing }
        coEvery { apiWrapper.filterCurrencies(null) } returns currencies

        //Act
        val foundCurrencies = runBlocking { homeDataSource.searchCurrencies() }

        //Assert
        Assert.assertEquals(currencies, foundCurrencies)
    }

}