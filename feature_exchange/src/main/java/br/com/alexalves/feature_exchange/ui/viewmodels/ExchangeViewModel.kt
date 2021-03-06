package br.com.alexalves.feature_exchange.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.alexalves.base.BaseViewModel
import br.com.alexalves.base.coroutines.AppContextProvider
import br.com.alexalves.feature_exchange.repository.ExchangeRepository
import br.com.alexalves.models.*
import br.com.alexalves.models.consts.AbbreviationCurrenciesConsts
import br.com.alexalves.models.exceptions.SaleNotApprovalException
import br.com.alexalves.models.exceptions.UserNotFoundException
import br.com.alexalves.utils.CurrencyUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger

class ExchangeViewModel(
    private val exchangeDataSource: ExchangeRepository
) : BaseViewModel() {

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
        CoroutineScope(AppContextProvider.io).launch {
            try {
                val user = exchangeDataSource.searchUser(userId)
                val amountCurrency = CurrencyUtils.filterCurrency(currency, user)
                val newFields = ScreenExchangeState
                    .InitExchangeFragment(currency, user.balance, amountCurrency)
                screenState.postValue(newFields)
                fields.postValue(newFields)
            } catch (error: Exception) {
                screenState.postValue(
                    ScreenExchangeState.FailureInInitExchangeFragment(
                        UserNotFoundException()
                    )
                )
            }
        }
    }

    fun purchaseCurrency(currency: Currency, quantity: BigInteger, userId: Long) {
        CoroutineScope(AppContextProvider.io).launch {
            try {
                val user = exchangeDataSource.searchUser(userId)
                updatePurchaseInUser(user, currency, quantity)
            } catch (error: Exception) {
                businessState.postValue(
                    BusinessExchangeState.Failure(
                        TypeOperation.PURCHASE,
                        UserNotFoundException()
                    )
                )
            }
        }
    }

    fun saleCurrency(currency: Currency, quantity: BigInteger, userId: Long) {
        CoroutineScope(AppContextProvider.io).launch {
            try {
                val user = exchangeDataSource.searchUser(userId)
                updateSaleInUser(user, currency, quantity)
            } catch (error: Exception) {
                businessState.postValue(
                    BusinessExchangeState.Failure(
                        TypeOperation.SALE,
                        UserNotFoundException()
                    )
                )
            }
        }
    }

    private fun updatePurchaseInUser(
        user: User,
        currency: Currency,
        quantity: BigInteger
    ) {
        CoroutineScope(AppContextProvider.io).launch {
            try {
                val totalPurchaseValue = quantity.toBigDecimal() * currency.buy
                val approval = (totalPurchaseValue <= user.balance)
                if (approval) {
                    savePurchaseInUser(quantity, currency, user)
                    exchangeDataSource.updateUser(user)

                    val message = configuraTextoCompra(quantity, currency, totalPurchaseValue)

                    businessState.postValue(
                        BusinessExchangeState.Sucess(TypeOperation.PURCHASE, message)
                    )

                } else businessState.postValue(
                    BusinessExchangeState.Failure(
                        TypeOperation.PURCHASE,
                        br.com.alexalves.models.exceptions.PurchaseNotApprovalException()
                    )
                )
            } catch (error: Exception) {
                businessState.postValue(
                    BusinessExchangeState.Failure(
                        TypeOperation.PURCHASE,
                        br.com.alexalves.models.exceptions.PurchaseNotApprovalException()
                    )
                )
            }
        }
    }

    private fun configuraTextoVenda(
        quantity: BigInteger,
        currency: Currency,
        totalValue: BigDecimal
    ) =
        "Parab??ns! \n" + "Voc?? acabou de vender ${quantity} ${currency?.abbreviation} - ${currency?.name}, totalizando\n" +
                CurrencyUtils.getFormattedValue_ToBRLCurrency(totalValue)


    private fun configuraTextoCompra(
        quantity: BigInteger,
        currency: Currency,
        totalValue: BigDecimal
    ) = "Parab??ns! \n" +
            "Voc?? acabou de comprar \n${quantity} ${currency?.abbreviation} - ${currency?.name}, totalizando \n" +
            "R\$ ${totalValue}"


    private fun updateSaleInUser(
        user: User,
        currency: Currency,
        quantity: BigInteger
    ) {
        CoroutineScope(AppContextProvider.io).launch {
            try {
                val currencyAmount = CurrencyUtils.filterCurrency(currency, user)
                val totalSaleValue = quantity.toBigDecimal() * currency.sell
                val approval = (quantity <= currencyAmount)
                if (approval) {
                    saveSaleInUser(quantity, currency, user)
                    exchangeDataSource.updateUser(user)

                    val message = configuraTextoVenda(quantity, currency, totalSaleValue)

                    businessState.postValue(
                        BusinessExchangeState.Sucess(
                            TypeOperation.SALE,
                            message
                        )
                    )
                } else {
                    businessState.postValue(
                        BusinessExchangeState.Failure(
                            TypeOperation.SALE,
                            SaleNotApprovalException()
                        )
                    )
                }
            } catch (error: Exception) {
                businessState.postValue(
                    BusinessExchangeState.Failure(
                        TypeOperation.SALE,
                        SaleNotApprovalException()
                    )
                )
            }
        }
    }

    private fun savePurchaseInUser(
        quantity: BigInteger,
        currency: Currency,
        user: User
    ) {
        val currencyAmount = CurrencyUtils.filterCurrency(currency, user)
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
        val currencyAmount = CurrencyUtils.filterCurrency(currency, user)
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
        currency: Currency,
        user: User
    ) {
        user.balance = finalBalance
        when (currency.abbreviation) {
            AbbreviationCurrenciesConsts.USD -> user.usd = finalCurrencyAmount
            AbbreviationCurrenciesConsts.EUR -> user.eur = finalCurrencyAmount
            AbbreviationCurrenciesConsts.GBP -> user.gbp = finalCurrencyAmount
            AbbreviationCurrenciesConsts.ARS -> user.ars = finalCurrencyAmount
            AbbreviationCurrenciesConsts.CAD -> user.cad = finalCurrencyAmount
            AbbreviationCurrenciesConsts.AUD -> user.aud = finalCurrencyAmount
            AbbreviationCurrenciesConsts.JPY -> user.jpy = finalCurrencyAmount
            AbbreviationCurrenciesConsts.CNY -> user.cny = finalCurrencyAmount
            AbbreviationCurrenciesConsts.BTC -> user.btc = finalCurrencyAmount
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
