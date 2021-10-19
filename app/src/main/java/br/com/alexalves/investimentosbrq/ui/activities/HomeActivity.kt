package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.consts.StaticConsts
import br.com.alexalves.investimentosbrq.consts.TextsConsts
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.HomeState
import br.com.alexalves.investimentosbrq.ui.adapter.CurrencyAdapter
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {
        homeViewModel.verifyExistingUser(StaticConsts.UserStaticID)
        configureToolbar()
        configureRecyclerView()
        observeCurrencies()
        homeViewModel.findCurrencies()
    }

    private fun observeCurrencies() {
        homeViewModel.viewHomeState.observe(this, Observer {
            when (it) {
                is HomeState.FoundCurrencies -> {
                    configureAdapter(it.currencies)
                }
                is HomeState.FailureInSearchCurrencies -> {
                    Log.e("ERRO", it.error.message.toString())
                }
            }
        })
    }

    private fun configureAdapter(currencies: List<Currency>) {
        val currencyAdapter = CurrencyAdapter(currencies, this, this::onClickItemMoedas)
        recyclerView.adapter = currencyAdapter
        currencyAdapter.notifyDataSetChanged()
    }

    private fun configureToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_investimentos)
        val titulo = findViewById<TextView>(R.id.toolbar_titulo)
        setSupportActionBar(toolbar)
        titulo.text = TextsConsts.TextMoedas
    }

    private fun configureRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_moedas_home)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun onClickItemMoedas(currency: Currency) {
        val intent = Intent(this, CambioActivity::class.java)
        intent.putExtra(getString(R.string.currency_argument), currency)
        startActivity(intent)
    }
}