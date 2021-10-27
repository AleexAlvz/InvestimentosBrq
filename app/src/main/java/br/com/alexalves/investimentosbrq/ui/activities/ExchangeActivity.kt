package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.alexalves.base.BaseActivity
import br.com.alexalves.investimentosbrq.consts.ArgumentConsts
import br.com.alexalves.investimentosbrq.consts.TextsConsts
import br.com.alexalves.investimentosbrq.databinding.ActivityExchangeBinding
import br.com.alexalves.investimentosbrq.model.BusinessExchangeState
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.TypeOperation
import br.com.alexalves.investimentosbrq.ui.fragments.ExchangeFragment
import br.com.alexalves.investimentosbrq.ui.fragments.OperationSucessFragment
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
        setSupportActionBar(binding.toolbarActivityCambio.toolbarInvestimentos)
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
        bundle.putSerializable("BusinessSucess", operationSucess)
        fragmentSucess.arguments = bundle

        fragmentSucess.buttonHomeListener = { voltarParaHome() }
        configureToolbarSucessFragment(operationSucess.typeOperation)
        replaceFragmentNoStack(binding.cambioContainer, fragmentSucess)
    }

    fun voltarParaHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun configureExchangeToolbar() {
        MainScope().launch {
            binding.toolbarActivityCambio.let {
                it.toolbarTitulo.text = TextsConsts.TextCambio
                it.toolbarBackOption.text = TextsConsts.TextMoedas
                it.toolbarBackOption.visibility = View.VISIBLE
                it.toolbarBackOption.setOnClickListener { voltarParaHome() }
             }
        }
    }

    fun configureToolbarSucessFragment(typeOperation: TypeOperation){
        MainScope().launch {
            binding.toolbarActivityCambio.let {
                it.toolbarTitulo.text = if (typeOperation == TypeOperation.PURCHASE) TextsConsts.TextComprar else TextsConsts.TextVender
                it.toolbarBackOption.text = TextsConsts.TextCambio
                it.toolbarBackOption.visibility = View.VISIBLE
                it.toolbarBackOption.setOnClickListener { startExchangeFragment() }
            }
        }
    }
}