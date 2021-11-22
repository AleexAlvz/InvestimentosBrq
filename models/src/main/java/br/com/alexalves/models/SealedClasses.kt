package br.com.alexalves.models

import java.io.Serializable
import java.lang.Exception
import java.math.BigDecimal
import java.math.BigInteger

sealed class BusinessExchangeState(): Serializable{
    data class Sucess(val typeOperation: TypeOperation, val message: String): BusinessExchangeState()
    data class Failure(val typeOperation: TypeOperation, val error: Exception): BusinessExchangeState()
}

sealed class ScreenExchangeState(){
    data class InitExchangeFragment(val currency: Currency, val userBalance: BigDecimal, val amountCurrency: BigInteger ): ScreenExchangeState()
    data class FailureInInitExchangeFragment(val error: Exception): ScreenExchangeState()
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