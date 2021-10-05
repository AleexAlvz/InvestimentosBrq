package br.com.alexalves.investimentosbrq.model

import java.lang.Exception
import java.math.BigDecimal
import java.math.BigInteger

sealed class BusinessExchangeState(){
    data class SucessPurchase(val amount: BigInteger, val value: BigDecimal): BusinessExchangeState()
    data class SucessSale(val amount: BigInteger, val value: BigDecimal): BusinessExchangeState()
    data class FailurePurchase(val error: Exception): BusinessExchangeState()
    data class FailureSale(val error: Exception): BusinessExchangeState()
    data class UserNotFound(val error: Exception): BusinessExchangeState()
}

sealed class ScreenExchangeState(){
    data class InitExchangeFragment(val currency: Currency, val userBalance: BigDecimal, val amountCurrency: BigInteger ): ScreenExchangeState()
    data class CurrencyNotFound(val error: Exception): ScreenExchangeState()
    data class UserNotFound(val error: Exception): ScreenExchangeState()
}

sealed class SellButtonEvent(){
    object Enabled: SellButtonEvent()
    object Disabled: SellButtonEvent()
}

sealed class BuyButtonEvent(){
    object Enabled: BuyButtonEvent()
    object Disabled: BuyButtonEvent()
}

sealed class HomeState(){
    data class FoundCurrencies(val currencies: List<Currency>): HomeState()
    data class FailureInSearchCurrencies(val error: Exception): HomeState()
}