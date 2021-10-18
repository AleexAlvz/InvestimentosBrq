package br.com.alexalves.investimentosbrq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.base.AppContextProvider
import br.com.alexalves.investimentosbrq.model.HomeState
import br.com.alexalves.investimentosbrq.model.exceptions.FailureInFoundCurrenciesException
import br.com.alexalves.investimentosbrq.repository.ExchangeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HomeViewModel(
    val exchangeDataSource: ExchangeRepository
) : ViewModel() {

    private val homeState = MutableLiveData<HomeState>()
    val viewHomeState: LiveData<HomeState> = homeState

    fun findCurrencies() {
        CoroutineScope(AppContextProvider.io).launch {
            try {
                val currencies = exchangeDataSource.searchCurrencies()
                if (currencies.isNotEmpty()){
                    homeState.postValue(HomeState.FoundCurrencies(currencies))
                } else {
                    homeState.postValue(HomeState.FailureInSearchCurrencies(FailureInFoundCurrenciesException("List isn't valid")))
                }
            } catch (error: Exception) {
                homeState.postValue(HomeState.FailureInSearchCurrencies(FailureInFoundCurrenciesException()))
            }
        }
    }
}