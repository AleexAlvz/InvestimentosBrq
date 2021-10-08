package br.com.alexalves.investimentosbrq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.model.HomeState
import br.com.alexalves.investimentosbrq.repository.ExchangeDataSource

class HomeViewModel(
    val exchangeDataSource: ExchangeDataSource
) : ViewModel() {

    private val homeEvent = MutableLiveData<HomeState>()
    val viewHomeState: LiveData<HomeState> = homeEvent

    fun buscaMoedas() {
        exchangeDataSource.searchCurrencies(
            whenSucess = { currencies ->
                homeEvent.value = HomeState.FoundCurrencies(currencies)
            }, whenFails = { error ->
                homeEvent.value = HomeState.FailureInSearchCurrencies(error)
            })
    }
}