package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.ui.fragments.CambioFragment

class CambioActivity : AppCompatActivity() {

    lateinit var toolbar_titulo: TextView
    lateinit var toolbar_voltar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio)
        inicializaCampos()
        configuraToolbar()
        inicializaFragmentCambio()
    }

    private fun inicializaFragmentCambio() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_cambio_container_fragment, CambioFragment())
            .commit()
    }

    private fun inicializaCampos() {
        toolbar_titulo = findViewById(R.id.toolbar_titulo)
        toolbar_voltar = findViewById(R.id.toolbar_back_option)
    }

    private fun configuraToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_investimentos)
        setSupportActionBar(toolbar)
        configuraToolbarEmCambio()
    }

    private fun configuraToolbarEmCambio() {
        toolbar_titulo.text = "Cambio"
        toolbar_voltar.text = "Moedas"
        toolbar_voltar.visibility = View.VISIBLE
        toolbar_voltar.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}