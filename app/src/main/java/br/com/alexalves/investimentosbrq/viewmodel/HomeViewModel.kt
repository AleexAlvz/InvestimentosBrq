package br.com.alexalves.investimentosbrq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.model.HomeState
import br.com.alexalves.investimentosbrq.repository.ExchangeDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class HomeViewModel(
    val exchangeDataSource: ExchangeDataSource
) : ViewModel() {

    private val homeEvent = MutableLiveData<HomeState>()
    val viewHomeState: LiveData<HomeState> = homeEvent

    fun findCurrencies() {
        CoroutineScope(IO).launch {
            try {
                val currencies = exchangeDataSource.searchCurrencies()
                homeEvent.postValue(HomeState.FoundCurrencies(currencies))
            } catch (error: Exception) {
                homeEvent.postValue(HomeState.FailureInSearchCurrencies(error))
            }
        }
    }
}