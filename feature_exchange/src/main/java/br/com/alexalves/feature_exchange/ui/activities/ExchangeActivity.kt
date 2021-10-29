package br.com.alexalves.feature_exchange.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.alexalves.base.BaseActivity
import br.com.alexalves.feature_exchange.R
import br.com.alexalves.feature_exchange.databinding.ActivityExchangeBinding
import br.com.alexalves.feature_exchange.ui.fragments.ExchangeFragment
import br.com.alexalves.feature_exchange.ui.fragments.OperationSucessFragment
import br.com.alexalves.models.Currency
import br.com.alexalves.models.consts.ArgumentConsts
import br.com.alexalves.models.consts.TextsConsts
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ExchangeActivity : BaseActivity() {

    lateinit var currency: Currency
    lateinit var binding: ActivityExchangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeBinding.inflate(layoutInflater)
        currency = intent.getSerializableExtra(ArgumentConsts.currency_argument) as Currency
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setSupportActionBar(findViewById(R.id.toolbar_activity_cambio))
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

    private fun iniciaSucessoCompraFragment(operationSucess: br.com.alexalves.models.BusinessExchangeState.Sucess){
        val fragmentSucess = OperationSucessFragment()

        val bundle = Bundle()
        bundle.putSerializable("BusinessSucess", operationSucess)
        fragmentSucess.arguments = bundle

        fragmentSucess.buttonHomeListener = { voltarParaHome() }
        configureToolbarSucessFragment(operationSucess.typeOperation)
        replaceFragmentNoStack(binding.cambioContainer, fragmentSucess)
    }

    fun voltarParaHome() {
        val intent = Intent()
        intent.setClassName(this, "br.com.alexalves.investimentosbrq.ui.activities.HomeActivity")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun configureExchangeToolbar() {
        MainScope().launch {
//            binding.toolbarActivityCambio.let {
//                it.text = TextsConsts.TextCambio
//                it.toolbarBackOption.text = TextsConsts.TextMoedas
//                it.toolbarBackOption.visibility = View.VISIBLE
//                it.toolbarBackOption.setOnClickListener { voltarParaHome() }
//             }
        }
    }

    fun configureToolbarSucessFragment(typeOperation: br.com.alexalves.models.TypeOperation){
        MainScope().launch {
//            binding.toolbarActivityCambio.let {
//                it.toolbarTitulo.text = if (typeOperation == br.com.alexalves.models.TypeOperation.PURCHASE) TextsConsts.TextComprar else TextsConsts.TextVender
//                it.toolbarBackOption.text = TextsConsts.TextCambio
//                it.toolbarBackOption.visibility = View.VISIBLE
//                it.toolbarBackOption.setOnClickListener { startExchangeFragment() }
//            }
        }
    }
}