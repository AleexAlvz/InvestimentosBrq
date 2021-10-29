package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alexalves.base.BaseActivity
import br.com.alexalves.feature_exchange.ui.activities.ExchangeActivity
import br.com.alexalves.investimentosbrq.databinding.ActivityHomeBinding
import br.com.alexalves.investimentosbrq.ui.adapter.CurrencyAdapter
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel
import br.com.alexalves.models.consts.ArgumentConsts
import br.com.alexalves.models.consts.StaticConsts
import br.com.alexalves.models.consts.TextsConsts
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding
    val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        homeViewModel.verifyExistingUser(StaticConsts.UserStaticID)
        configureToolbar()
        observeCurrencies()
        homeViewModel.findCurrencies()
    }

    private fun observeCurrencies() {
        homeViewModel.viewHomeState.observe(this, Observer {
            when (it) {
                is br.com.alexalves.models.HomeState.FoundCurrencies -> { configureRecyclerView(it.currencies) }
                is br.com.alexalves.models.HomeState.FailureInSearchCurrencies -> { Log.e("ERRO", it.error.message.toString()) }
            }
        })
    }

    private fun configureRecyclerView(currencies: List<br.com.alexalves.models.Currency>) {
        binding.recyclerViewMoedasHome.let {
            it.layoutManager = LinearLayoutManager(this)
            val currencyAdapter = CurrencyAdapter(currencies, this, this::onClickItemMoedas)
            it.adapter = currencyAdapter
            currencyAdapter.notifyDataSetChanged()
        }
    }

    private fun configureToolbar() {
        binding.toolbarActivityHome.toolbarInvestimentos

        binding.toolbarActivityHome.let {
            setSupportActionBar(it.toolbarInvestimentos)
            it.toolbarTitulo.text = TextsConsts.TextMoedas
        }
    }

    fun onClickItemMoedas(currency: br.com.alexalves.models.Currency) {
        Intent().let {
            it.setClassName(this, "br.com.alexalves.feature_exchange.ui.activities.ExchangeActivity")
            it.putExtra(ArgumentConsts.currency_argument, currency)
            startActivity(it)
        }
    }

}