package br.com.alexalves.investimentosbrq.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.consts.StaticConsts
import br.com.alexalves.investimentosbrq.model.*
import br.com.alexalves.investimentosbrq.ui.customview.ButtonBlue
import br.com.alexalves.investimentosbrq.utils.CurrencyUtils
import br.com.alexalves.investimentosbrq.viewmodel.ExchangeViewModel
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal
import java.math.BigInteger

class ExchangeFragment : Fragment() {

    lateinit var inflatedView: View
    var sucessPurchase: ((quantityPurchased: BigInteger, purchaseValue: BigDecimal) -> Unit)? = null
    var sucessSale: ((quantitySold: BigInteger, saleValue: BigDecimal) -> Unit)? = null
    val exchangeViewModel: ExchangeViewModel by viewModel()
    val userId = StaticConsts.UserStaticID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cambio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflatedView = view
        init()
    }

    private fun init() {
        observerStates()
        initCambioFragment()
    }
    private fun initCambioFragment() {
        val currency = arguments?.get(getString(R.string.currency_argument)) as Currency
        exchangeViewModel.initCambioFragment(currency = currency, userId = userId)
    }
    private fun initCambioComponents(fields: ScreenExchangeState.InitExchangeFragment) {
        initTextFields(fields)
        configureBusinessButtons(fields)
    }
    private fun initTextFields(fields: ScreenExchangeState.InitExchangeFragment) {

//      Titulo
        val textTituloMoeda =
            inflatedView.findViewById<TextView>(R.id.fragment_cambio_text_titulo_moeda)
        val tituloFormatado = "${fields.currency.abbreviation} - ${fields.currency.name}"
        textTituloMoeda.text = tituloFormatado

        //Variacao
        val textVariacaoMoeda =
            inflatedView.findViewById<TextView>(R.id.fragment_cambio_text_variacao_moeda)
        textVariacaoMoeda.text = CurrencyUtils().getVariacaoFormatada(fields.currency.variation)
        val color = CurrencyUtils().getCurrencyColor(fields.currency.variation, requireContext())
        textVariacaoMoeda.setTextColor(color)

        //Compra
        val textValorCompraMoeda =
            inflatedView.findViewById<TextView>(R.id.fragment_cambio_text_valor_compra_moeda)
        val buyValue = CurrencyUtils().getValorCompraFormatado(fields.currency)
        val buyValueFormated = "Compra: $buyValue"
        textValorCompraMoeda.text = buyValueFormated

        //Venda
        val textValorVendaMoeda =
            inflatedView.findViewById<TextView>(R.id.fragment_cambio_text_valor_venda_moeda)
        val sellValue = CurrencyUtils().getValorVendaFormatado(fields.currency)
        val sellValueFormated = "Venda: $sellValue"
        textValorVendaMoeda.text = sellValueFormated

        //Saldo
        val textUserBalance =
            inflatedView.findViewById<TextView>(R.id.fragment_cambio_text_saldo_disponivel)
        val userBalanceFormated = "Saldo disponível: R$ ${fields.userBalance}"
        textUserBalance.text = userBalanceFormated

        //Moedas em caixa
        val textAmountCurrency =
            inflatedView.findViewById<TextView>(R.id.fragment_cambio_text_moeda_em_caixa)
        val amountCurrencyFormated = "${fields.amountCurrency} ${fields.currency.name} em caixa"
        textAmountCurrency.text = amountCurrencyFormated
    }

    private fun observerStates() {
        observerBusinessState()
        observerScreenState()
        observerSellButtonEvent()
        observerBuyButtonEvent()
    }
    private fun observerBuyButtonEvent() {
        exchangeViewModel.viewBuyButtonEvent.observe(viewLifecycleOwner, {
            when (it) {
                is BuyButtonEvent.Enabled -> {
                    configureBuyButtonState(true)
                }
                is BuyButtonEvent.Disabled -> {
                    configureBuyButtonState(false)
                }
            }
        })
    }

    private fun observerSellButtonEvent() {
        exchangeViewModel.viewSellButtonEvent.observe(viewLifecycleOwner, {
            when (it) {
                is SellButtonEvent.Enabled -> {
                    configureSellButtonState(true)
                }
                is SellButtonEvent.Disabled -> {
                    configureSellButtonState(false)
                }
            }
        })
    }

    private fun observerScreenState() {
        exchangeViewModel.viewScreenState.observe(viewLifecycleOwner, {
            when (it) {
                is ScreenExchangeState.InitExchangeFragment -> {
                    initCambioComponents(it)
                }
            }
        })
    }

    private fun observerBusinessState() {
        exchangeViewModel.viewBusinessExchangeState.observe(viewLifecycleOwner, {
            when (it) {
                is BusinessExchangeState.SucessPurchase -> {
                    sucessPurchase?.invoke(it.amount, it.value)
                }
                is BusinessExchangeState.SucessSale -> {
                    sucessSale?.invoke(it.amount, it.value)
                }
            }
        })
    }

    private fun configureBusinessButtons(fields: ScreenExchangeState.InitExchangeFragment) {
        val buyButton = inflatedView.findViewById<ButtonBlue>(R.id.fragment_cambio_button_comprar)
        val sellButton = inflatedView.findViewById<ButtonBlue>(R.id.fragment_cambio_button_vender)
        val inputLayoutQuantidade =
            inflatedView.findViewById<TextInputLayout>(R.id.fragment_cambio_input_layout_quantidade)

        configureInitStateButtons(buyButton, sellButton)
        configureOnTextQuantidadeChanged(inputLayoutQuantidade)

        buyButton.configuraClique = {
            val amount = inputLayoutQuantidade.editText?.text.toString().toBigInteger()
            exchangeViewModel.purchaseCurrency(fields.currency, amount, userId)
        }

        sellButton.configuraClique = {
            val amount = inputLayoutQuantidade.editText?.text.toString().toBigInteger()
            exchangeViewModel.saleCurrency(fields.currency, amount, userId)
        }
    }

    private fun configureBuyButtonState(state: Boolean) {
        val buyButton = inflatedView.findViewById<ButtonBlue>(R.id.fragment_cambio_button_comprar)
        buyButton.configuraEstado(state)
    }

    private fun configureSellButtonState(state: Boolean) {
        val sellButton = inflatedView.findViewById<ButtonBlue>(R.id.fragment_cambio_button_vender)
        sellButton.configuraEstado(state)
    }

    private fun configureOnTextQuantidadeChanged(inputLayoutQuantidade: TextInputLayout) {
        inputLayoutQuantidade.editText?.doAfterTextChanged { s ->
            val amountText = s.toString()
            exchangeViewModel.configureBuyButtonEvent(amountText)
            exchangeViewModel.configureSellButtonEvent(amountText)
        }
    }

    private fun configureInitStateButtons(buttonComprar: ButtonBlue, buttonVender: ButtonBlue) {
        buttonComprar.configuraTitulo("Comprar")
        buttonComprar.configuraEstado(false)
        buttonVender.configuraTitulo("Vender")
        buttonVender.configuraEstado(false)
    }

}
