package br.com.alexalves.investimentosbrq.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.model.*
import br.com.alexalves.investimentosbrq.repository.CurrencyRepository
import br.com.alexalves.investimentosbrq.utils.CurrencyUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger

class ExchangeViewModel(
    val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val fields = MutableLiveData<ScreenExchangeState.InitExchangeFragment>()

    private val businessState = MutableLiveData<BusinessExchangeState>()
    val viewBusinessExchangeState: LiveData<BusinessExchangeState> = businessState

    private val screenState = MutableLiveData<ScreenExchangeState>()
    val viewScreenState = screenState

    private val buyButtonEvent = MutableLiveData<BuyButtonEvent>()
    val viewBuyButtonEvent = buyButtonEvent

    private val sellButtonEvent = MutableLiveData<SellButtonEvent>()
    val viewSellButtonEvent = sellButtonEvent



    fun saleCurrency(currency: Currency, quantity: BigInteger) {
        currencyRepository.searchUser(1L,
            whenSucess = { user ->
                updateSaleInUser(user, currency, quantity)
            }, whenFails = { error ->
                MainScope().launch {
                    businessState.value = BusinessExchangeState.UserNotFound(error)
                }
            })
    }

    fun purchaseCurrency(currency: Currency, quantity: BigInteger) {
        currencyRepository.searchUser(1L,
            whenSucess = { user -> updatePurchaseInUser(user, currency, quantity) },
            whenFails = { error ->
                MainScope().launch {
                    businessState.value = BusinessExchangeState.UserNotFound(error)
                }
            })
    }

    private fun updateSaleInUser(
        user: User,
        currency: Currency,
        quantity: BigInteger
    ) {
        try {
            CoroutineScope(IO).launch {
                val balance = user.balance
                val currencyAmount = CurrencyUtils().filterCurrency(currency, user)
                val totalSaleValue = quantity.toBigDecimal() * currency.sell

                val approval = (quantity <= currencyAmount)
                if (approval) {
                    val finalBalance = balance + totalSaleValue
                    val finalCurrencyAmount = currencyAmount - quantity
                    saveBusinessChangeInUser(
                        finalBalance,
                        finalCurrencyAmount,
                        currency,
                        user
                    )
                    currencyRepository.updateUser(
                        user,
                        whenSucess = {
                            MainScope().launch {
                                businessState.value =
                                    BusinessExchangeState.SucessSale(quantity, totalSaleValue)
                            }
                        }, whenFails = { error ->
                            MainScope().launch {
                                businessState.value = BusinessExchangeState.FailureSale(error)
                            }
                        })
                } else {
                    MainScope().launch {
                        businessState.value =
                            BusinessExchangeState.FailureSale(Exception("Operation not approved"))
                    }
                }
            }
        } catch (error: Exception) {
            MainScope().launch {
                businessState.value =
                    BusinessExchangeState.FailureSale(Exception("Fails in sale"))
            }
        }
    }


    private fun updatePurchaseInUser(
        user: User,
        currency: Currency,
        quantity: BigInteger
    ) {
        try {
            CoroutineScope(IO).launch {
                val balance = user.balance
                val currencyAmount = CurrencyUtils().filterCurrency(currency, user)
                val totalPurchaseValue = quantity.toBigDecimal() * currency.buy

                val approval = (totalPurchaseValue <= balance)
                if (approval) {
                    val finalBalance = balance - totalPurchaseValue
                    val finalCurrencyAmount = currencyAmount + quantity
                    saveBusinessChangeInUser(
                        finalBalance,
                        finalCurrencyAmount,
                        currency,
                        user
                    )
                    currencyRepository.updateUser(
                        user,
                        whenSucess = {
                            MainScope().launch {
                                businessState.value = BusinessExchangeState.SucessPurchase(
                                    quantity,
                                    totalPurchaseValue
                                )
                            }
                        }, whenFails = { error ->
                            MainScope().launch {
                                businessState.value =
                                    BusinessExchangeState.FailurePurchase(error)
                            }
                        })
                } else {
                    MainScope().launch {
                        businessState.value =
                            BusinessExchangeState.FailurePurchase(Exception("Não aprovado"))
                    }
                }
            }
        } catch (erro: Exception) {
            MainScope().launch {
                businessState.value =
                    BusinessExchangeState.FailurePurchase(Exception("Fails in purchase"))
            }
        }
    }

    fun initCambioFragment(currency: Currency, userId: Long) {
        currencyRepository.searchUser(userId,
            whenSucess = { user ->
                try {
                    val amountCurrency = CurrencyUtils().filterCurrency(currency, user)
                    val newFields = ScreenExchangeState.InitExchangeFragment(
                        currency = currency,
                        userBalance = user.balance,
                        amountCurrency = amountCurrency
                    )
                    screenState.value = newFields
                    this.fields.value = newFields
                } catch (error: Exception) {
                    screenState.value = ScreenExchangeState.CurrencyNotFound(error)
                }
            }, whenFails = {
                screenState.value = ScreenExchangeState.UserNotFound(it)
            }
        )
    }

    fun configureBuyButtonEvent(amountText: String) {
        if (amountText.isBlank()) {
            buyButtonEvent.value = BuyButtonEvent.Disabled
            return
        } else {
            if (fields.value != null) {
                val fields = fields.value!!
                val requiredAmount = amountText.toBigDecimal() * fields.currency.buy
                val approval =
                    ((requiredAmount <= fields.userBalance) && amountText.toBigInteger() > BigInteger.ZERO)
                if (approval) {
                    buyButtonEvent.value = BuyButtonEvent.Enabled
                    return
                } else buyButtonEvent.value = BuyButtonEvent.Disabled
            }
        }
    }

    fun configureSellButtonEvent(amountText: String) {
        if (amountText.isBlank()) {
            sellButtonEvent.value = SellButtonEvent.Disabled
        } else {
            if (fields.value != null) {
                val amount = amountText.toBigInteger()
                val fields = fields.value!!
                val approval = ((amount <= fields.amountCurrency) && (amount > BigInteger.ZERO))
                if (approval) {
                    sellButtonEvent.value = SellButtonEvent.Enabled
                    return
                } else sellButtonEvent.value = SellButtonEvent.Disabled
            }
        }
    }

    private fun saveBusinessChangeInUser(
        finalBalance: BigDecimal,
        finalCurrencyAmount: BigInteger,
        currency: Currency,
        user: User
    ) {
        user.balance = finalBalance
        when (currency.abbreviation) {
            "USD" -> user.usd = finalCurrencyAmount
            "EUR" -> user.eur = finalCurrencyAmount
            "GBP" -> user.gbp = finalCurrencyAmount
            "ARS" -> user.ars = finalCurrencyAmount
            "CAD" -> user.cad = finalCurrencyAmount
            "AUD" -> user.aud = finalCurrencyAmount
            "JPY" -> user.jpy = finalCurrencyAmount
            "CNY" -> user.cny = finalCurrencyAmount
            "BTC" -> user.btc = finalCurrencyAmount
        }
    }

}
