package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.consts.TextsConsts
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.TypeOperation
import br.com.alexalves.investimentosbrq.ui.fragments.ExchangeFragment
import br.com.alexalves.investimentosbrq.ui.fragments.OperationSucessFragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.math.BigDecimal
import java.math.BigInteger

class CambioActivity : AppCompatActivity() {

    lateinit var toolbar_titulo: TextView
    lateinit var toolbar_voltar: TextView
    lateinit var currency: Currency

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio)
        init()
    }

    private fun init() {
        inicializaCampos()
        configuraToolbar()
        inicializaFragment(fragmentCambioConfigurado())
    }

    private fun fragmentCambioConfigurado(): ExchangeFragment {
        val cambioFragment = ExchangeFragment()
        configuraQuandoSucessoCompraCambio(cambioFragment)
        configuraQuandoSucessoVendaCambio(cambioFragment)
        configuraBundleMoedaEmCambioFragment(cambioFragment)
        return cambioFragment
    }

    private fun configuraBundleMoedaEmCambioFragment(exchangeFragment: ExchangeFragment) {
        val bundle = Bundle()
        bundle.putSerializable(getString(R.string.currency_argument), currency)
        exchangeFragment.arguments = bundle
    }

    private fun configuraQuandoSucessoVendaCambio(exchangeFragment: ExchangeFragment) {
        exchangeFragment.sucessSale = { quantidadeVendida, valorDaCompra ->
            val fragmentVenda = fragmentVendaSucedidaConfigurado(quantidadeVendida, valorDaCompra)
            configuraToolbarEmVendaSucesso()
            inicializaFragment(fragmentVenda)
        }
    }

    private fun configuraQuandoSucessoCompraCambio(exchangeFragment: ExchangeFragment) {
        exchangeFragment.sucessPurchase = { quantidadeComprada, valorDaCompra ->
            val fragmentCompra = fragmentCompraSucedidaConfigurado(quantidadeComprada, valorDaCompra)
            configuraToolbarEmCompraSucesso()
            inicializaFragment(fragmentCompra)
        }
    }

    private fun fragmentVendaSucedidaConfigurado(
        quantidadeVendida: BigInteger,
        valorTotal: BigDecimal
    ): Fragment {
        val fragmentVenda = OperationSucessFragment()
        fragmentVenda.quantidade = quantidadeVendida
        fragmentVenda.valorTotal = valorTotal
        fragmentVenda.currency = currency
        fragmentVenda.typeOperation = TypeOperation.SALE
        fragmentVenda.buttonHomeListener = { voltarParaHome() }
        return fragmentVenda
    }

    private fun fragmentCompraSucedidaConfigurado(
        quantidadeComprada: BigInteger,
        valorTotal: BigDecimal
    ): Fragment {
        val fragmentCompra = OperationSucessFragment()
        fragmentCompra.quantidade = quantidadeComprada
        fragmentCompra.valorTotal = valorTotal
        fragmentCompra.currency = currency
        fragmentCompra.typeOperation = TypeOperation.PURCHASE
        fragmentCompra.buttonHomeListener = { voltarParaHome() }
        return fragmentCompra
    }

    fun voltarParaHome() {
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
        intent.getSerializableExtra(getString(R.string.currency_argument))?.let { moedaExtra ->
            currency = moedaExtra as Currency
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
        MainScope().launch {
            toolbar_titulo.text = TextsConsts.ExchangeToolbarTitleText
            toolbar_voltar.text = TextsConsts.ExchangeToolbarBackText
            toolbar_voltar.visibility = View.VISIBLE
            toolbar_voltar.setOnClickListener { voltarParaHome() }
        }
    }

    private fun configuraToolbarEmVendaSucesso() {
        MainScope().launch {
            toolbar_titulo.text = TextsConsts.OperationVendaToolbarTitleText
            toolbar_voltar.text = TextsConsts.OperationSucessToolbarBackText
            toolbar_voltar.visibility = View.VISIBLE
            toolbar_voltar.setOnClickListener {
                configuraToolbarEmCambio()
                inicializaFragment(fragmentCambioConfigurado())
            }
        }
    }

    private fun configuraToolbarEmCompraSucesso() {
        MainScope().launch {
            toolbar_titulo.text = TextsConsts.OperationComprarToolbarTitleText
            toolbar_voltar.text = TextsConsts.OperationSucessToolbarBackText
            toolbar_voltar.visibility = View.VISIBLE
            toolbar_voltar.setOnClickListener {
                configuraToolbarEmCambio()
                inicializaFragment(fragmentCambioConfigurado())
            }
        }
    }
}