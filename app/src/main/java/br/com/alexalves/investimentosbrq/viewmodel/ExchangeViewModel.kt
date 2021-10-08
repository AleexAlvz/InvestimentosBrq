package br.com.alexalves.investimentosbrq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.consts.AbbreviationCurrenciesConsts
import br.com.alexalves.investimentosbrq.model.*
import br.com.alexalves.investimentosbrq.repository.ExchangeDataSource
import br.com.alexalves.investimentosbrq.repository.ExchangeRepository
import br.com.alexalves.investimentosbrq.utils.CurrencyUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger

class ExchangeViewModel(
    private val exchangeDataSource: ExchangeRepository
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

    fun initCambioFragment(currency: Currency, userId: Long) {
        exchangeDataSource.searchUser(userId) {
            when (it) {
                is OperateUser.Success -> {
                    try {
                        val amountCurrency = CurrencyUtils().filterCurrency(currency, it.user)
                        val newFields = ScreenExchangeState.InitExchangeFragment(
                            currency = currency,
                            userBalance = it.user.balance,
                            amountCurrency = amountCurrency
                        )
                        screenState.value = newFields
                        fields.value = newFields
                    } catch (error: Exception) {
                        screenState.value = ScreenExchangeState.CurrencyNotFound(error)
                    }
                }
                is OperateUser.Error -> {
                    screenState.value = ScreenExchangeState.UserNotFound(it.error)
                }
            }
        }
    }

    fun purchaseCurrency(currency: Currency, quantity: BigInteger) {
        exchangeDataSource.searchUser(1L) {
            when (it) {
                is OperateUser.Success -> {
                    updatePurchaseInUser(it.user, currency, quantity)
                }
                is OperateUser.Error -> {
                    screenState.value = ScreenExchangeState.UserNotFound(it.error)
                }
            }
        }
    }

    fun saleCurrency(currency: Currency, quantity: BigInteger) {
        exchangeDataSource.searchUser(1L) {
            when (it) {
                is OperateUser.Success -> {
                    updateSaleInUser(it.user, currency, quantity)
                }
                is OperateUser.Error -> {
                    screenState.value = ScreenExchangeState.UserNotFound(it.error)
                }
            }
        }
    }

    private fun updatePurchaseInUser(
        user: User,
        currency: Currency,
        quantity: BigInteger
    ) {
        val totalPurchaseValue = quantity.toBigDecimal() * currency.buy
        val approval = (totalPurchaseValue <= user.balance)
        if (approval) {
            savePurchaseInUser(quantity, currency, user)
            exchangeDataSource.updateUser(user) {
                when (it) {
                    is OperateUser.Success -> {
                        businessState.value =
                            BusinessExchangeState.SucessPurchase(quantity, totalPurchaseValue)
                    }
                    is OperateUser.Error -> {
                        businessState.value =
                            BusinessExchangeState.FailurePurchase(it.error)
                    }
                }
            }
        }
    }

    private fun updateSaleInUser(
        user: User,
        currency: Currency,
        quantity: BigInteger
    ) {
        val currencyAmount = CurrencyUtils().filterCurrency(currency, user)
        val totalSaleValue = quantity.toBigDecimal() * currency.sell

        val approval = (quantity <= currencyAmount)
        if (approval) {
            saveSaleInUser(quantity, currency, user)
            exchangeDataSource.updateUser(user) {
                when (it) {
                    is OperateUser.Success -> {
                        businessState.value =
                            BusinessExchangeState.SucessSale(quantity, totalSaleValue)
                    }
                    is OperateUser.Error -> {
                        businessState.value = BusinessExchangeState.FailureSale(it.error)
                    }
                }
            }
        }
    }

    private fun savePurchaseInUser(
        quantity: BigInteger,
        currency: Currency,
        user: User
    ) {
        val currencyAmount = CurrencyUtils().filterCurrency(currency, user)
        val totalPurchaseValue = quantity.toBigDecimal() * currency.buy
        val finalBalance = user.balance - totalPurchaseValue
        val finalCurrencyAmount = currencyAmount + quantity
        saveBusinessChangeInUser(
            finalBalance,
            finalCurrencyAmount,
            currency,
            user
        )
    }

    private fun saveSaleInUser(
        quantity: BigInteger,
        currency: Currency,
        user: User
    ) {
        val balance = user.balance
        val currencyAmount = CurrencyUtils().filterCurrency(currency, user)
        val totalSaleValue = quantity.toBigDecimal() * currency.sell
        val finalBalance = balance + totalSaleValue
        val finalCurrencyAmount = currencyAmount - quantity
        saveBusinessChangeInUser(
            finalBalance,
            finalCurrencyAmount,
            currency,
            user
        )
    }

    private fun saveBusinessChangeInUser(
        finalBalance: BigDecimal,
        finalCurrencyAmount: BigInteger,
        currency: Currency,
        user: User
    ) {
        user.balance = finalBalance
        when (currency.abbreviation) {
            AbbreviationCurrenciesConsts().USD -> user.usd = finalCurrencyAmount
            AbbreviationCurrenciesConsts().EUR -> user.eur = finalCurrencyAmount
            AbbreviationCurrenciesConsts().GBP -> user.gbp = finalCurrencyAmount
            AbbreviationCurrenciesConsts().ARS -> user.ars = finalCurrencyAmount
            AbbreviationCurrenciesConsts().CAD -> user.cad = finalCurrencyAmount
            AbbreviationCurrenciesConsts().AUD -> user.aud = finalCurrencyAmount
            AbbreviationCurrenciesConsts().JPY -> user.jpy = finalCurrencyAmount
            AbbreviationCurrenciesConsts().CNY -> user.cny = finalCurrencyAmount
            AbbreviationCurrenciesConsts().BTC -> user.btc = finalCurrencyAmount
        }
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

}
