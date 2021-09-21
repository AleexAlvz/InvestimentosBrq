package br.com.alexalves.investimentosbrq.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.repository.MoedasRepository
import br.com.alexalves.investimentosbrq.ui.adapter.MoedasAdapter

class HomeActivity: AppCompatActivity() {

    lateinit var moedasRepository: MoedasRepository
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        configuraToolbar()
        inicializaCampos()
        configuraRecyclerView()
    }

    private fun configuraAdapter() {
        moedasRepository = MoedasRepository()
        moedasRepository.moedas.observe(
            this,
            Observer { moedas ->
                val moedasAdapter = MoedasAdapter(moedas, this, this::onClickItemMoedas)
                recyclerView.adapter = moedasAdapter
                moedasAdapter.notifyDataSetChanged()
            })
        moedasRepository.buscaMoedas()
    }

    private fun inicializaCampos() {
        moedasRepository = MoedasRepository()
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
        Log.i("Click",moeda.name)
    }
}