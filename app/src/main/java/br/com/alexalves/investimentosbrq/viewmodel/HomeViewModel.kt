package br.com.alexalves.investimentosbrq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.alexalves.base.BaseViewModel
import br.com.alexalves.base.coroutines.AppContextProvider
import br.com.alexalves.base.repository.HomeRepository
import br.com.alexalves.models.HomeState
import br.com.alexalves.models.exceptions.FailureInFoundCurrenciesException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(
    private val exchangeDataSource: HomeRepository
) : BaseViewModel() {

    private val homeState = MutableLiveData<HomeState>()
    val viewHomeState: LiveData<HomeState> = homeState

    fun findCurrencies() {
        CoroutineScope(AppContextProvider.io).launch {
            try {
                val currencies = exchangeDataSource.searchCurrencies()
                if (currencies.isNotEmpty()){
                    homeState.postValue(HomeState.FoundCurrencies(currencies))
                } else {
                    homeState.postValue(
                        HomeState.FailureInSearchCurrencies(
                            FailureInFoundCurrenciesException("List isn't valid")
                        ))
                }
            } catch (error: Exception) {
                homeState.postValue(HomeState.FailureInSearchCurrencies(FailureInFoundCurrenciesException()))
            }
        }
    }

    fun verifyExistingUser(userId: Long) {
        CoroutineScope(AppContextProvider.io).launch{
            val user = exchangeDataSource.searchUser(userId)
            if (user==null){
                exchangeDataSource.saveUser(br.com.alexalves.models.User(id = userId))
            }
        }
    }
}