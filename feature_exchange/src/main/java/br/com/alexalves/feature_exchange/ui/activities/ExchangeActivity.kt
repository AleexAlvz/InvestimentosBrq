package br.com.alexalves.feature_exchange.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.alexalves.base.BaseActivity
import br.com.alexalves.feature_exchange.databinding.ActivityExchangeBinding
import br.com.alexalves.feature_exchange.ui.fragments.ExchangeFragment
import br.com.alexalves.feature_exchange.ui.fragments.OperationSucessFragment
import br.com.alexalves.models.BusinessExchangeState
import br.com.alexalves.models.Currency
import br.com.alexalves.models.TypeOperation
import br.com.alexalves.models.consts.AccessibilityConsts
import br.com.alexalves.models.consts.ArgumentConsts
import br.com.alexalves.models.consts.TextsConsts
import br.com.alexalves.models.consts.UIConsts
import br.com.alexalves.utils.extensions.TextViewExtensions.setAccessibleText
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ExchangeActivity : BaseActivity() {

    private lateinit var currency: Currency
    private lateinit var binding: ActivityExchangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeBinding.inflate(layoutInflater)
        currency = intent.getSerializableExtra(ArgumentConsts.currency_argument) as Currency
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setSupportActionBar(binding.toolbarActivityCambio.exchangeToolbar)
        startExchangeFragment()
    }

    private fun startExchangeFragment() {
        configureExchangeToolbar()
        replaceFragmentNoStack(binding.cambioContainer, getExchangeFragmentConfigured())
    }

    private fun getExchangeFragmentConfigured(): ExchangeFragment {
        val exchangeFragment = ExchangeFragment()
        //Configure events
        exchangeFragment.businessSucessCallBack = { sucess -> iniciaSucessoCompraFragment(sucess) }
        //Configure Bundle with currency
        val bundle = Bundle()
        bundle.putSerializable(ArgumentConsts.currency_argument, currency)
        exchangeFragment.arguments = bundle

        return exchangeFragment
    }

    private fun iniciaSucessoCompraFragment(operationSucess: BusinessExchangeState.Sucess){
        val fragmentSucess = OperationSucessFragment()

        val bundle = Bundle()
        bundle.putSerializable(ArgumentConsts.business_sucess_argument, operationSucess)
        fragmentSucess.arguments = bundle

        fragmentSucess.buttonHomeListener = { voltarParaHome() }
        configureToolbarSucessFragment(operationSucess.typeOperation)
        replaceFragmentNoStack(binding.cambioContainer, fragmentSucess)
    }

    fun voltarParaHome() {
        val intent = Intent()
        intent.setClassName(this, UIConsts.homeActivityDirectory)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun configureExchangeToolbar() {
        MainScope().launch {
            binding.toolbarActivityCambio.let {
                it.toolbarTitulo.text = TextsConsts.TextCambio
                it.toolbarBackOption.setAccessibleText(TextsConsts.TextMoedas, AccessibilityConsts.messageBeforeButtonBack)
                it.toolbarBackOption.visibility = View.VISIBLE
                it.toolbarBackOption.setOnClickListener { voltarParaHome() }
             }
        }
    }

    fun configureToolbarSucessFragment(typeOperation: TypeOperation){
        MainScope().launch {
            binding.toolbarActivityCambio.let {
                it.toolbarTitulo.text = if (typeOperation == TypeOperation.PURCHASE) TextsConsts.TextComprar else TextsConsts.TextVender
                it.toolbarBackOption.setAccessibleText(TextsConsts.TextCambio, AccessibilityConsts.messageBeforeButtonBack)
                it.toolbarBackOption.visibility = View.VISIBLE
                it.toolbarBackOption.setOnClickListener { startExchangeFragment() }
            }
        }
    }
}