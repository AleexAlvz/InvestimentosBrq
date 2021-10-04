package br.com.alexalves.investimentosbrq.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.ui.customview.ButtonBlue
import br.com.alexalves.investimentosbrq.viewmodel.CambioViewModel
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal
import java.math.BigInteger

class CambioFragment : Fragment() {

    private lateinit var moeda: Moeda
    private lateinit var inputLayoutQuantidade: TextInputLayout
    private lateinit var buttonComprar: ButtonBlue
    private lateinit var buttonVender: ButtonBlue
    var sucessoCompra: ((quantidadeComprada: BigInteger, valorDaCompra: BigDecimal) -> Unit)? = null
    var sucessoVenda: ((quantidadeVendida: BigInteger, valorDaCompra: BigDecimal) -> Unit)? = null
    val cambioViewModel: CambioViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cambio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializaCampos(view)
        configuraSaldoEMoedas(view)
        configuraTextoNosCampos(view)
    }

    private fun configuraSaldoEMoedas(view: View) {
        cambioViewModel.buscaSaldoEMoedasEmCaixa(
            moeda,
            atualizaSaldoEMoedas = { saldo, moedasEmCaixa ->
                //Configura Text Saldo
                val saldoFormatado = "Saldo disponível: R$ ${saldo.toString().replace(".", ",")}"
                view.findViewById<TextView>(R.id.fragment_cambio_text_saldo_disponivel).text =
                    saldoFormatado
                //Configura Text Moedas em Caixa
                val moedaEmCaixaFormatado = "${moedasEmCaixa} ${moeda.name} em caixa"
                view.findViewById<TextView>(R.id.fragment_cambio_text_moeda_em_caixa).text =
                    moedaEmCaixaFormatado
                configuraButtons(moedasEmCaixa, saldo)
            })
    }

    private fun configuraButtons(moedasEmCaixa: BigInteger, saldo: BigDecimal) {
        //Configuracao inicial dos buttons
        buttonVender.configuraEstado(false)
        buttonVender.configuraTitulo("Vender")
        buttonComprar.configuraEstado(false)
        buttonComprar.configuraTitulo("Comprar")
        configuraCampoQuantidadeChangedTextListener(moedasEmCaixa, saldo)
        buttonCompraListener()
        buttonVendaListener()
    }

    private fun buttonVendaListener() {
        buttonVender.configuraClique = {
            val quantidadeParaVender =
                inputLayoutQuantidade.editText?.text.toString().toBigInteger()
            cambioViewModel.vendeMoeda(
                moeda,
                quantidadeParaVender,
                quandoSucesso = { totalDaVenda ->
                    sucessoVenda?.invoke(
                        quantidadeParaVender,
                        totalDaVenda
                    )
                },
            )
        }
    }

    private fun buttonCompraListener() {
        buttonComprar.configuraClique = {
            val textQuantidade = inputLayoutQuantidade.editText?.text.toString()
            val quantidadeParaComprar =
                if (textQuantidade.isBlank()) BigInteger.ZERO else textQuantidade.toBigInteger()
            cambioViewModel.compraMoeda(
                moeda,
                quantidadeParaComprar,
                quandoSucesso = { totalDaCompra ->
                    Log.i("ERRO", "ERRO")
                    sucessoCompra?.invoke(
                        quantidadeParaComprar,
                        totalDaCompra
                    )
                },
            )
        }
    }

    private fun configuraCampoQuantidadeChangedTextListener(
        moedasEmCaixa: BigInteger,
        saldo: BigDecimal
    ) {
        inputLayoutQuantidade.editText?.doAfterTextChanged { s ->
            val quantidadeTexto = s.toString()
            if (quantidadeTexto.isBlank()) {
                buttonComprar.configuraEstado(false)
                buttonVender.configuraEstado(false)
            } else {
                val quantidadeInt = quantidadeTexto.toBigInteger()
                configuraButtonVenda(quantidadeInt, moedasEmCaixa)
                configuraButtonCompra(quantidadeInt, saldo)
            }
        }
    }

    private fun configuraButtonCompra(quantidade: BigInteger, saldo: BigDecimal) {
        val valorNecessario = quantidade.toBigDecimal() * moeda.buy
        val aprovacao = ((valorNecessario <= saldo) && quantidade > BigInteger.ZERO)
        buttonComprar.configuraEstado(aprovacao)
    }

    private fun configuraButtonVenda(quantidade: BigInteger, moedasEmCaixa: BigInteger) {
        val aprovacao = ((quantidade <= moedasEmCaixa) && (quantidade != BigInteger.ZERO))
        buttonVender.configuraEstado(aprovacao)
    }

    private fun configuraTextoNosCampos(view: View) {
        //Titulo
        val textTituloMoeda = view.findViewById<TextView>(R.id.fragment_cambio_text_titulo_moeda)
        val tituloFormatado = "${moeda.abreviacao} - ${moeda.name}"
        textTituloMoeda.text = tituloFormatado
        //Variacao
        val textVariacaoMoeda =
            view.findViewById<TextView>(R.id.fragment_cambio_text_variacao_moeda)
        textVariacaoMoeda.text = moeda.getVariacaoFormatada()
        setColorVariacao(textVariacaoMoeda)
        //Compra
        val textValorCompraMoeda =
            view.findViewById<TextView>(R.id.fragment_cambio_text_valor_compra_moeda)
        val valorCompraFormatado = "Compra: ${moeda.getValorCompraFormatado()}"
        textValorCompraMoeda.text = valorCompraFormatado
        //Venda
        val textValorVendaMoeda =
            view.findViewById<TextView>(R.id.fragment_cambio_text_valor_venda_moeda)
        val valorVendaFormatado = "Venda: ${moeda.getValorVendaFormatado()}"
        textValorVendaMoeda.text = valorVendaFormatado
        //Saldo
        val textSaldoUsuario =
            view.findViewById<TextView>(R.id.fragment_cambio_text_saldo_disponivel)
        textSaldoUsuario.text = "Saldo disponível: R$ 0"
        //Moedas em caixa
        val textMoedasEmCaixa =
            view.findViewById<TextView>(R.id.fragment_cambio_text_moeda_em_caixa)
        val moedasEmCaixaFormatado = "0 ${moeda.name} em caixa"
        textMoedasEmCaixa.text = moedasEmCaixaFormatado
    }

    private fun setColorVariacao(textViewVariacao: TextView) {
        cambioViewModel.buscaCorMoeda(moeda.variation, requireContext())
            .observe(viewLifecycleOwner, Observer
            { colorId ->
                textViewVariacao.setTextColor(colorId)
            })
    }

    private fun inicializaCampos(view: View) {
        moeda = arguments?.get(getString(R.string.moeda_argument)) as Moeda
        inputLayoutQuantidade = view.findViewById(R.id.fragment_cambio_input_layout_quantidade)
        buttonComprar = view.findViewById(R.id.fragment_cambio_button_comprar)
        buttonVender = view.findViewById(R.id.fragment_cambio_button_vender)
    }
}
