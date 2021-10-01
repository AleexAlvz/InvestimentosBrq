package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.model.TipoOperacao
import br.com.alexalves.investimentosbrq.ui.fragments.CambioFragment
import br.com.alexalves.investimentosbrq.ui.fragments.OperacaoSucedidaFragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger

class CambioActivity : AppCompatActivity() {

    lateinit var toolbar_titulo: TextView
    lateinit var toolbar_voltar: TextView
    lateinit var moeda: Moeda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio)
        inicializaCampos()
        configuraToolbar()
        inicializaFragment(fragmentCambioConfigurado())
    }

    private fun fragmentCambioConfigurado(): CambioFragment {
        val cambioFragment = CambioFragment()
        configuraQuandoSucessoCompraCambio(cambioFragment)
        configuraQuandoSucessoVendaCambio(cambioFragment)
        configuraBundleMoedaEmCambioFragment(cambioFragment)
        return cambioFragment
    }

    private fun configuraBundleMoedaEmCambioFragment(cambioFragment: CambioFragment) {
        val bundle = Bundle()
        bundle.putSerializable(getString(R.string.moeda_argument), moeda)
        cambioFragment.arguments = bundle
    }

    private fun configuraQuandoSucessoVendaCambio(cambioFragment: CambioFragment) {
        cambioFragment.sucessoVenda = { quantidadeVendida, valorDaCompra ->
            val fragmentVenda = fragmentVendaSucedidaConfigurado(quantidadeVendida, valorDaCompra)
            configuraToolbarEmVendaSucesso()
            inicializaFragment(fragmentVenda)
        }
    }

    private fun configuraQuandoSucessoCompraCambio(cambioFragment: CambioFragment) {
        cambioFragment.sucessoCompra = { quantidadeComprada, valorDaCompra ->
            val fragmentCompra = fragmentCompraSucedidaConfigurado(quantidadeComprada, valorDaCompra)
            configuraToolbarEmCompraSucesso()
            inicializaFragment(fragmentCompra)
        }
    }

    private fun fragmentVendaSucedidaConfigurado(quantidadeVendida: BigInteger, valorTotal: BigDecimal): Fragment {
        val fragmentVenda = OperacaoSucedidaFragment()
        fragmentVenda.quantidade = quantidadeVendida
        fragmentVenda.valorTotal = valorTotal
        fragmentVenda.moeda = moeda
        fragmentVenda.tipoOperacao = TipoOperacao.Venda
        fragmentVenda.buttonHomeListener = { voltarParaHome() }
        return fragmentVenda
    }

    private fun fragmentCompraSucedidaConfigurado(quantidadeComprada: BigInteger, valorTotal: BigDecimal): Fragment {
        val fragmentCompra = OperacaoSucedidaFragment()
        fragmentCompra.quantidade = quantidadeComprada
        fragmentCompra.valorTotal = valorTotal
        fragmentCompra.moeda = moeda
        fragmentCompra.tipoOperacao = TipoOperacao.Compra
        fragmentCompra.buttonHomeListener = { voltarParaHome() }
        return fragmentCompra
    }

    fun voltarParaHome(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun inicializaFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_cambio_container_fragment, fragment)
            .commit()
    }

    private fun inicializaCampos() {
        if (intent.hasExtra(getString(R.string.moeda_argument))){
            moeda = intent.getSerializableExtra(getString(R.string.moeda_argument)) as Moeda
        }
        toolbar_titulo = findViewById(R.id.toolbar_titulo)
        toolbar_voltar = findViewById(R.id.toolbar_back_option)
    }

    private fun configuraToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_investimentos)
        setSupportActionBar(toolbar)
        configuraToolbarEmCambio()
    }

    private fun configuraToolbarEmCambio() {
        val context: Context = this
        MainScope().launch {
            toolbar_titulo.text = "Cambio"
            toolbar_voltar.text = "Moedas"
            toolbar_voltar.visibility = View.VISIBLE
            toolbar_voltar.setOnClickListener {
                val intent = Intent(context, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun configuraToolbarEmVendaSucesso() {
        MainScope().launch {
            toolbar_titulo.text = "Vender"
            toolbar_voltar.text = "Câmbio"
            toolbar_voltar.visibility = View.VISIBLE
            toolbar_voltar.setOnClickListener {
                configuraToolbarEmCambio()
                inicializaFragment(fragmentCambioConfigurado())
            }
        }
    }

    private fun configuraToolbarEmCompraSucesso() {
        MainScope().launch {
            toolbar_titulo.text = "Comprar"
            toolbar_voltar.text = "Câmbio"
            toolbar_voltar.visibility = View.VISIBLE
            toolbar_voltar.setOnClickListener {
                configuraToolbarEmCambio()
                inicializaFragment(fragmentCambioConfigurado())
            }
        }
    }
}