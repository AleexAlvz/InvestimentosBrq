package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alexalves.base.BaseActivity
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.consts.ArgumentConsts
import br.com.alexalves.investimentosbrq.consts.StaticConsts
import br.com.alexalves.investimentosbrq.consts.TextsConsts
import br.com.alexalves.investimentosbrq.databinding.ActivityHomeBinding
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.HomeState
import br.com.alexalves.investimentosbrq.ui.adapter.CurrencyAdapter
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel
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
                is HomeState.FoundCurrencies -> { configureRecyclerView(it.currencies) }
                is HomeState.FailureInSearchCurrencies -> { Log.e("ERRO", it.error.message.toString()) }
            }
        })
    }

    private fun configureRecyclerView(currencies: List<Currency>) {
        binding.recyclerViewMoedasHome.let {
            it.layoutManager = LinearLayoutManager(this)
            val currencyAdapter = CurrencyAdapter(currencies, this, this::onClickItemMoedas)
            it.adapter = currencyAdapter
            currencyAdapter.notifyDataSetChanged()
        }
    }

    private fun configureToolbar() {
        binding.toolbarActivityHome.let {
            setSupportActionBar(it.toolbarInvestimentos)
            it.toolbarTitulo.text = TextsConsts.TextMoedas
        }
    }

    fun onClickItemMoedas(currency: Currency) {
        Intent(this, ExchangeActivity::class.java).let {
            it.putExtra(ArgumentConsts.currency_argument, currency)
            startActivity(it)
        }
    }

}