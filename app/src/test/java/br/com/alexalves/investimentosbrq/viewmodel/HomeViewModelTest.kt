package br.com.alexalves.investimentosbrq.viewmodel

import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest : TestCase(){

    @Test
    fun test_WhenFoundCurrencies_thenUpdateHomeEvent(){

        val homeViewModel = mock(HomeViewModel::class.java)

        homeViewModel.findCurrencies()

    }
}