package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.alexalves.investimentosbrq.HomeViewModel
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.ui.adapter.MoedasAdapter

class HomeActivity: AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        configuraToolbar()
        inicializaCampos()
        configuraRecyclerView()
    }

    private fun configuraAdapter() {

        viewModel.buscaMoedas(
            quandoSucesso = { moedas ->
                val moedasAdapter = MoedasAdapter(moedas, this, this::onClickItemMoedas)
                recyclerView.adapter = moedasAdapter
                moedasAdapter.notifyDataSetChanged()
        }, quandoFalha = {erro ->
                Log.i("ERRO",erro)
        })
    }

    private fun inicializaCampos() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
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
        intent.putExtra(getString(R.string.parametro_moeda), moeda)
        startActivity(intent)
    }
}