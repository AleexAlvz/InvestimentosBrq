package br.com.alexalves.investimentosbrq.datasources

import br.com.alexalves.base.database.UserDAO
import br.com.alexalves.feature_exchange.repository.ExchangeDataSource
import br.com.alexalves.models.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
    lateinit var exchangeDataSource: ExchangeDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        exchangeDataSource = ExchangeDataSource(userDao)
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
}