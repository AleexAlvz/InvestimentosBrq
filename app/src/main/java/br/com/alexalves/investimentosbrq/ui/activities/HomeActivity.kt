package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.ui.adapter.MoedasAdapter
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity: AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        configuraToolbar()
        inicializaCampos()
        configuraRecyclerView()
    }

    private fun configuraAdapter() {
        homeViewModel.listaDeMoedas.observe(this, Observer { moedas ->
            val moedasAdapter = MoedasAdapter(moedas, this, this::onClickItemMoedas)
            recyclerView.adapter = moedasAdapter
            moedasAdapter.notifyDataSetChanged()
        })
        homeViewModel.buscaMoedas()
    }

    private fun inicializaCampos() {
        recyclerView = findViewById(R.id.recycler_view_moedas_home)
    }

    private fun configuraToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_investimentos)
        val titulo = findViewById<TextView>(R.id.toolbar_titulo)
        setSupportActionBar(toolbar)
        titulo.text = "Moedas"
    }

    private fun configuraRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        configuraAdapter()
    }

    fun onClickItemMoedas(moeda: Moeda){
        val intent = Intent(this, CambioActivity::class.java)
        intent.putExtra(getString(R.string.moeda_argument), moeda)
        startActivity(intent)
    }
}