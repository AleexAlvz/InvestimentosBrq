package br.com.alexalves.feature_exchange.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import br.com.alexalves.base.BaseFragment
import br.com.alexalves.feature_exchange.databinding.FragmentExchangeBinding
import br.com.alexalves.feature_exchange.ui.customview.ButtonBlue
import br.com.alexalves.feature_exchange.ui.viewmodels.ExchangeViewModel
import br.com.alexalves.models.consts.ArgumentConsts
import br.com.alexalves.models.consts.StaticConsts
import br.com.alexalves.utils.CurrencyUtils
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeFragment : BaseFragment() {

    lateinit var binding: FragmentExchangeBinding
    var businessSucessCallBack: ((bussinesSucess: br.com.alexalves.models.BusinessExchangeState.Sucess) -> Unit)? = null
    val exchangeViewModel: ExchangeViewModel by viewModel()
    val userId = StaticConsts.UserStaticID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExchangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        observerStates()
        initCambioFragment()
    }

    private fun initCambioFragment() {
        val currency = arguments?.get(ArgumentConsts.currency_argument) as br.com.alexalves.models.Currency
        exchangeViewModel.initCambioFragment(currency = currency, userId = userId)
    }

    private fun initCambioComponents(fields: br.com.alexalves.models.ScreenExchangeState.InitExchangeFragment) {
        initTextFields(fields)
        configureBusinessButtons(fields)
    }

    private fun initTextFields(fields: br.com.alexalves.models.ScreenExchangeState.InitExchangeFragment) {
        binding.let {
            //Title
            val tituloFormatado = "${fields.currency.abbreviation} - ${fields.currency.name}"
            it.fragmentCambioTextTituloMoeda.text = tituloFormatado
            //Variation
            it.fragmentCambioTextVariacaoMoeda.text =
                CurrencyUtils().getFormattedVariation(fields.currency.variation)
            it.fragmentCambioTextVariacaoMoeda.setTextColor(
                CurrencyUtils().getCurrencyColor(
                    fields.currency.variation,
                    requireContext()
                )
            )
            //Buy
            val buyValue = CurrencyUtils().getFormattedPurchaseValue(fields.currency)
            val buyValueFormated = "Compra: $buyValue"
            it.fragmentCambioTextValorCompraMoeda.text = buyValueFormated
            //Sell
            val sellValue = CurrencyUtils().getFormattedSaleValue(fields.currency)
            val sellValueFormated = "Venda: $sellValue"
            it.fragmentCambioTextValorVendaMoeda.text = sellValueFormated
            //Balance
            val userBalanceFormated =
                "Saldo disponível: ${CurrencyUtils().getFormattedValue_ToBRLCurrency(fields.userBalance)}"
            it.fragmentCambioTextSaldoDisponivel.text = userBalanceFormated
            //AmountCurrency
            val amountCurrencyFormated = "${fields.amountCurrency} ${fields.currency.name} em caixa"
            it.fragmentCambioTextMoedaEmCaixa.text = amountCurrencyFormated
        }
    }

    private fun observerStates() {
        observerBusinessState()
        observerScreenState()
        observerSellButtonEvent()
        observerBuyButtonEvent()
    }

    private fun observerBuyButtonEvent() {
        exchangeViewModel.viewBuyButtonEvent.observe(viewLifecycleOwner) {
            when (it) {
                is br.com.alexalves.models.BuyButtonEvent.Enabled -> {
                    binding.fragmentCambioButtonComprar.configuraEstado(true)
                }
                is br.com.alexalves.models.BuyButtonEvent.Disabled -> {
                    binding.fragmentCambioButtonComprar.configuraEstado(false)
                }
            }
        }
    }

    private fun observerSellButtonEvent() {
        exchangeViewModel.viewSellButtonEvent.observe(viewLifecycleOwner, {
            when (it) {
                is br.com.alexalves.models.SellButtonEvent.Enabled -> {
                    binding.fragmentCambioButtonVender.configuraEstado(true)
                }
                is br.com.alexalves.models.SellButtonEvent.Disabled -> {
                    binding.fragmentCambioButtonVender.configuraEstado(false)
                }
            }
        })
    }

    private fun observerScreenState() {
        exchangeViewModel.viewScreenState.observe(viewLifecycleOwner, {
            when (it) {
                is br.com.alexalves.models.ScreenExchangeState.InitExchangeFragment -> {
                    initCambioComponents(it)
                }
            }
        })
    }

    private fun observerBusinessState() {
        exchangeViewModel.viewBusinessExchangeState.observe(viewLifecycleOwner, {
            when (it) {
                is br.com.alexalves.models.BusinessExchangeState.Sucess -> {
                    businessSucessCallBack?.invoke(it)
                }
                is br.com.alexalves.models.BusinessExchangeState.Failure -> { }
            }
        })
    }

    private fun configureBusinessButtons(fields: br.com.alexalves.models.ScreenExchangeState.InitExchangeFragment) {
        val buyButton = binding.fragmentCambioButtonComprar
        val sellButton = binding.fragmentCambioButtonVender
        val inputLayoutQuantidade = binding.fragmentCambioInputLayoutQuantidade

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
