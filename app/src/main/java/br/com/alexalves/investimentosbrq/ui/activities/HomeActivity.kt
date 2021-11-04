package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alexalves.base.BaseActivity
import br.com.alexalves.investimentosbrq.databinding.ActivityHomeBinding
import br.com.alexalves.investimentosbrq.ui.adapter.CurrencyAdapter
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel
import br.com.alexalves.models.Currency
import br.com.alexalves.models.HomeState
import br.com.alexalves.models.consts.ArgumentConsts
import br.com.alexalves.models.consts.StaticConsts
import br.com.alexalves.models.consts.TextsConsts
import br.com.alexalves.models.consts.UIConsts
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()

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
        homeViewModel.viewHomeState.observe(this, {
            when (it) {
                is HomeState.FoundCurrencies -> { configureRecyclerView(it.currencies) }
                is HomeState.FailureInSearchCurrencies -> { Log.e(TextsConsts.Erro, it.error.message.toString()) }
            }
        })
    }

    private fun configureRecyclerView(currencies: List<Currency>) {
        binding.recyclerViewMoedasHome.let {
            it.layoutManager = LinearLayoutManager(this)
            val currencyAdapter = CurrencyAdapter(currencies, this, this::onClickItemCurrency)
            it.adapter = currencyAdapter
            currencyAdapter.notifyDataSetChanged()
        }
    }

    private fun configureToolbar() {
        binding.toolbarActivityHome.let {
            setSupportActionBar(it.homeToolbar)
            it.toolbarTitulo.text = TextsConsts.TextMoedas
        }
    }

    private fun onClickItemCurrency(currency: Currency) {
        Intent().let {
            it.setClassName(this, UIConsts.exchangeActivityDirectory)
            it.putExtra(ArgumentConsts.currency_argument, currency)
            startActivity(it)
        }
    }

}