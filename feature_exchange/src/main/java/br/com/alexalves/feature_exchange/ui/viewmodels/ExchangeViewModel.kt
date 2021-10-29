package br.com.alexalves.feature_exchange.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.alexalves.base.BaseViewModel
import br.com.alexalves.base.repository.ExchangeRepository
import br.com.alexalves.models.consts.AbbreviationCurrenciesConsts
import br.com.alexalves.utils.CurrencyUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger

class ExchangeViewModel(
    private val exchangeDataSource: ExchangeRepository
) : BaseViewModel() {

    private val fields = MutableLiveData<br.com.alexalves.models.ScreenExchangeState.InitExchangeFragment>()

    private val businessState = MutableLiveData<br.com.alexalves.models.BusinessExchangeState>()
    val viewBusinessExchangeState: LiveData<br.com.alexalves.models.BusinessExchangeState> = businessState

    private val screenState = MutableLiveData<br.com.alexalves.models.ScreenExchangeState>()
    val viewScreenState = screenState

    private val buyButtonEvent = MutableLiveData<br.com.alexalves.models.BuyButtonEvent>()
    val viewBuyButtonEvent = buyButtonEvent

    private val sellButtonEvent = MutableLiveData<br.com.alexalves.models.SellButtonEvent>()
    val viewSellButtonEvent = sellButtonEvent

    fun initCambioFragment(currency: br.com.alexalves.models.Currency, userId: Long) {
        CoroutineScope(br.com.alexalves.base.coroutines.AppContextProvider.io).launch {
            try {
                val user = exchangeDataSource.searchUser(userId)
                val amountCurrency = CurrencyUtils()
                    .filterCurrency(currency, user)
                val newFields = br.com.alexalves.models.ScreenExchangeState
                    .InitExchangeFragment(currency, user.balance, amountCurrency)
                screenState.postValue(newFields)
                fields.postValue(newFields)
            } catch (error: Exception) {
                screenState.postValue(
                    br.com.alexalves.models.ScreenExchangeState.FailureInInitExchangeFragment(
                        br.com.alexalves.models.exceptions.UserNotFoundException()
                    )
                )
            }
        }
    }

    fun purchaseCurrency(currency: br.com.alexalves.models.Currency, quantity: BigInteger, userId: Long) {
        CoroutineScope(br.com.alexalves.base.coroutines.AppContextProvider.io).launch {
            try {
                val user = exchangeDataSource.searchUser(userId)
                updatePurchaseInUser(user, currency, quantity)
            } catch (error: Exception) {
                businessState.postValue(
                    br.com.alexalves.models.BusinessExchangeState.Failure(
                        br.com.alexalves.models.TypeOperation.PURCHASE,
                        br.com.alexalves.models.exceptions.UserNotFoundException()
                    )
                )
            }
        }
    }

    fun saleCurrency(currency: br.com.alexalves.models.Currency, quantity: BigInteger, userId: Long) {
        CoroutineScope(br.com.alexalves.base.coroutines.AppContextProvider.io).launch {
            try {
                val user = exchangeDataSource.searchUser(userId)
                updateSaleInUser(user, currency, quantity)
            } catch (error: Exception) {
                businessState.postValue(
                    br.com.alexalves.models.BusinessExchangeState.Failure(
                        br.com.alexalves.models.TypeOperation.SALE,
                        br.com.alexalves.models.exceptions.UserNotFoundException()
                    )
                )
            }
        }
    }

    private fun updatePurchaseInUser(
        user: br.com.alexalves.models.User,
        currency: br.com.alexalves.models.Currency,
        quantity: BigInteger
    ) {
        CoroutineScope(br.com.alexalves.base.coroutines.AppContextProvider.io).launch {
            try {
                val totalPurchaseValue = quantity.toBigDecimal() * currency.buy
                val approval = (totalPurchaseValue <= user.balance)
                if (approval) {
                    savePurchaseInUser(quantity, currency, user)
                    exchangeDataSource.updateUser(user)

                    val message = configuraTextoCompra(quantity, currency, totalPurchaseValue)

                    businessState.postValue(
                        br.com.alexalves.models.BusinessExchangeState.Sucess(br.com.alexalves.models.TypeOperation.PURCHASE, message)
                    )

                } else businessState.postValue(
                    br.com.alexalves.models.BusinessExchangeState.Failure(
                        br.com.alexalves.models.TypeOperation.PURCHASE,
                        br.com.alexalves.models.exceptions.PurchaseNotApprovalException()
                    )
                )
            } catch (error: Exception) {
                businessState.postValue(
                    br.com.alexalves.models.BusinessExchangeState.Failure(
                        br.com.alexalves.models.TypeOperation.PURCHASE,
                        br.com.alexalves.models.exceptions.PurchaseNotApprovalException()
                    )
                )
            }
        }
    }

    private fun configuraTextoVenda(
        quantity: BigInteger,
        currency: br.com.alexalves.models.Currency,
        totalValue: BigDecimal
    ) =
        "Parabéns! \n" + "Você acabou de vender ${quantity} ${currency?.abbreviation} - ${currency?.name}, totalizando\n" +
                br.com.alexalves.utils.CurrencyUtils().getFormattedValue_ToBRLCurrency(totalValue)


    private fun configuraTextoCompra(
        quantity: BigInteger,
        currency: br.com.alexalves.models.Currency,
        totalValue: BigDecimal
    ) = "Parabéns! \n" +
            "Você acabou de comprar ${quantity} ${currency?.abbreviation} - ${currency?.name}, totalizando \n" +
            "R\$ ${totalValue}"


    private fun updateSaleInUser(
        user: br.com.alexalves.models.User,
        currency: br.com.alexalves.models.Currency,
        quantity: BigInteger
    ) {
        CoroutineScope(br.com.alexalves.base.coroutines.AppContextProvider.io).launch {
            try {
                val currencyAmount = br.com.alexalves.utils.CurrencyUtils()
                    .filterCurrency(currency, user)
                val totalSaleValue = quantity.toBigDecimal() * currency.sell
                val approval = (quantity <= currencyAmount)
                if (approval) {
                    saveSaleInUser(quantity, currency, user)
                    exchangeDataSource.updateUser(user)

                    val message = configuraTextoVenda(quantity, currency, totalSaleValue)

                    businessState.postValue(
                        br.com.alexalves.models.BusinessExchangeState.Sucess(
                            br.com.alexalves.models.TypeOperation.SALE,
                            message
                        )
                    )
                } else {
                    businessState.postValue(
                        br.com.alexalves.models.BusinessExchangeState.Failure(
                            br.com.alexalves.models.TypeOperation.SALE,
                            br.com.alexalves.models.exceptions.SaleNotApprovalException()
                        )
                    )
                }
            } catch (error: Exception) {
                businessState.postValue(
                    br.com.alexalves.models.BusinessExchangeState.Failure(
                        br.com.alexalves.models.TypeOperation.SALE,
                        br.com.alexalves.models.exceptions.SaleNotApprovalException()
                    )
                )
            }
        }
    }

    private fun savePurchaseInUser(
        quantity: BigInteger,
        currency: br.com.alexalves.models.Currency,
        user: br.com.alexalves.models.User
    ) {
        val currencyAmount = br.com.alexalves.utils.CurrencyUtils().filterCurrency(currency, user)
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
        currency: br.com.alexalves.models.Currency,
        user: br.com.alexalves.models.User
    ) {
        val currencyAmount = br.com.alexalves.utils.CurrencyUtils().filterCurrency(currency, user)
        val totalSaleValue = quantity.toBigDecimal() * currency.sell
        val finalBalance = user.balance + totalSaleValue
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
        currency: br.com.alexalves.models.Currency,
        user: br.com.alexalves.models.User
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
            buyButtonEvent.value = br.com.alexalves.models.BuyButtonEvent.Disabled
            return
        } else {
            if (fields.value != null) {
                val fields = fields.value!!
                val requiredAmount = amountText.toBigDecimal() * fields.currency.buy
                val approval =
                    ((requiredAmount <= fields.userBalance) && amountText.toBigInteger() > BigInteger.ZERO)
                if (approval) {
                    buyButtonEvent.value = br.com.alexalves.models.BuyButtonEvent.Enabled
                    return
                } else buyButtonEvent.value = br.com.alexalves.models.BuyButtonEvent.Disabled
            }
        }
    }

    fun configureSellButtonEvent(amountText: String) {
        if (amountText.isBlank()) {
            sellButtonEvent.value = br.com.alexalves.models.SellButtonEvent.Disabled
        } else {
            if (fields.value != null) {
                val amount = amountText.toBigInteger()
                val fields = fields.value!!
                val approval = ((amount <= fields.amountCurrency) && (amount > BigInteger.ZERO))
                if (approval) {
                    sellButtonEvent.value = br.com.alexalves.models.SellButtonEvent.Enabled
                    return
                } else sellButtonEvent.value = br.com.alexalves.models.SellButtonEvent.Disabled
            }
        }
    }

}
