package br.com.alexalves.investimentosbrq.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.database.DatabaseBuilder
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.ui.customview.ButtonBlue
import br.com.alexalves.investimentosbrq.viewmodel.CambioViewModel
import br.com.alexalves.investimentosbrq.viewmodel.viewModelFactory.CambioViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal

class CambioFragment : Fragment() {

    private lateinit var inflatedView: View
    private lateinit var moeda: Moeda
    private lateinit var textTituloMoeda: TextView
    private lateinit var textVariacaoMoeda: TextView
    private lateinit var textValorCompraMoeda: TextView
    private lateinit var textValorVendaMoeda: TextView
    private lateinit var textSaldoUsuario: TextView
    private lateinit var textMoedasEmCaixa: TextView
    private lateinit var inputLayoutQuantidade: TextInputLayout
    private lateinit var buttonComprar: ButtonBlue
    private lateinit var buttonVender: ButtonBlue
    private lateinit var cambioViewModel: CambioViewModel
    private var moedasEmCaixa: Int = 0
    private var saldoUsuario: BigDecimal = BigDecimal.ZERO
    private var sucessoBuscaUsuario: Boolean = false
    var sucessoCompra: ((quantidadeComprada: Int, valorDaCompra: BigDecimal) -> Unit)? = null
    var sucessoVenda: ((quantidadeVendida: Int, valorDaCompra: BigDecimal) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflatedView = inflater.inflate(R.layout.fragment_cambio, container, false)
        inicializaCampos()
        observaUsuario()
        buscaDadosUsuario()
        configuraTextoNosCampos()
        return inflatedView
    }

    private fun configuraButtons() {
        //Configuracao inicial dos buttons
        buttonVender.configuraEstado(false)
        buttonVender.configuraTitulo("Vender")
        Log.i("TESTE",(moeda.buy<saldoUsuario).toString())
        Log.i("TESTE","Saldo: ${saldoUsuario}")
        Log.i("TESTE","Valor moeda: ${moeda.buy}")
        buttonComprar.configuraEstado(moeda.buy < saldoUsuario)
        buttonComprar.configuraTitulo("Comprar")
        configuraCampoQuantidadeChangedTextListener()
        //Eventos dos botões
        buttonCompraListener()
        buttonVendaListener()
    }

    private fun buttonVendaListener() {
        buttonVender.configuraClique = {
            val quantidadeParaVender = inputLayoutQuantidade.editText?.text.toString().toInt()
            cambioViewModel.vendeMoeda(
                moeda,
                quantidadeParaVender,
                quandoSucesso = { totalDaVenda ->
                    sucessoVenda?.invoke(
                        quantidadeParaVender,
                        totalDaVenda
                    )
                },
                quandoFalha = { erro ->
                    Toast.makeText(requireContext(), erro, Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun buttonCompraListener() {
        buttonComprar.configuraClique = {
            val textQuantidade = inputLayoutQuantidade.editText?.text.toString()
            val quantidadeParaComprar = if (textQuantidade.isBlank()) 0  else textQuantidade.toInt()
            cambioViewModel.compraMoeda(
                moeda,
                quantidadeParaComprar,
                quandoSucesso = { totalDaCompra ->
                    Log.i("ERRO","ERRO")
                    sucessoCompra?.invoke(
                        quantidadeParaComprar,
                        totalDaCompra
                    )
                },
                quandoFalha = { erro ->
                    Log.i("ERRO", erro)
                }
            )
        }
    }

    private fun configuraCampoQuantidadeChangedTextListener() {
        inputLayoutQuantidade.editText?.doAfterTextChanged { s ->
            val quantidadeTexto = s.toString()
            if (quantidadeTexto.isBlank()) {
                buttonComprar.configuraEstado(true)
                buttonVender.configuraEstado(false)
            } else {
                val quantidadeInt = quantidadeTexto.toInt()
                configuraButtonVenda(quantidadeInt)
                configuraButtonCompra(quantidadeInt)
            }
        }
    }

    private fun configuraButtonCompra(quantidade: Int) {
        val valorNecessario = quantidade.toBigDecimal() * moeda.buy
        val aprovacao = ((valorNecessario <= saldoUsuario))
        buttonComprar.configuraEstado(aprovacao)
    }

    private fun configuraButtonVenda(quantidade: Int) {
        val aprovacao = ((quantidade <= moedasEmCaixa) && (quantidade != 0))
        buttonVender.configuraEstado(aprovacao)
    }

    private fun buscaDadosUsuario() {
        cambioViewModel.atualizaSaldoEmCaixa(moeda)
        cambioViewModel.atualizaSaldo()
    }

    private fun observaUsuario() {
        observaSaldoUsuario()
        observaMoedasEmCaixaUsuario()
    }

    private fun observaMoedasEmCaixaUsuario() {
        cambioViewModel.moedasEmCaixa.observe(viewLifecycleOwner, Observer { moedaEmCaixaBuscada ->
            if (moedaEmCaixaBuscada != null) {
                moedasEmCaixa = moedaEmCaixaBuscada
                val moedaEmCaixaFormatado = "${moedasEmCaixa} ${moeda.name} em caixa"
                textMoedasEmCaixa.text = moedaEmCaixaFormatado
                if (sucessoBuscaUsuario){
                    configuraButtons()
                } else sucessoBuscaUsuario = true
            }
        })
    }

    private fun observaSaldoUsuario() {
        cambioViewModel.saldoUsuario.observe(viewLifecycleOwner, Observer { saldoBuscado ->
            if (saldoBuscado != null) {
                saldoUsuario = saldoBuscado
                val saldoFormatado =
                    "Saldo disponível: R$ ${saldoBuscado.toString().replace(".", ",")}"
                textSaldoUsuario.text = saldoFormatado
                if (sucessoBuscaUsuario){
                    configuraButtons()
                } else sucessoBuscaUsuario = true
            }
        })
    }

    private fun configuraTextoNosCampos() {
        //Titulo
        val tituloFormatado = "${moeda.abreviacao} - ${moeda.name}"
        textTituloMoeda.text = tituloFormatado
        //Variacao
        textVariacaoMoeda.text = moeda.getVariacaoFormatada()
        setVariacaoColor()
        //Compra
        val valorCompraFormatado = "Compra: ${moeda.getValorCompraFormatado()}"
        textValorCompraMoeda.text = valorCompraFormatado
        //Venda
        val valorVendaFormatado = "Venda: ${moeda.getValorVendaFormatado()}"
        textValorVendaMoeda.text = valorVendaFormatado
        //Saldo
        textSaldoUsuario.text = "Saldo disponível: R$ 0"
        //Moedas em caixa
        val moedasEmCaixaFormatado = "0 ${moeda.name} em caixa"
        textMoedasEmCaixa.text = moedasEmCaixaFormatado
    }

    private fun setVariacaoColor() {
        if (moeda.variation > 0) {
            textVariacaoMoeda.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green_positive
                )
            )
        } else if (moeda.variation < 0) {
            textVariacaoMoeda.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red_negative
                )
            )
        } else textVariacaoMoeda.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }

    private fun inicializaCampos() {
        //Busca moeda no bundle do fragment
        moeda = arguments?.get(getString(R.string.moeda_argument)) as Moeda
        //busca textViews do Card Moeda
        textTituloMoeda = inflatedView.findViewById(R.id.fragment_cambio_text_titulo_moeda)
        textVariacaoMoeda = inflatedView.findViewById(R.id.fragment_cambio_text_variacao_moeda)
        textValorCompraMoeda =
            inflatedView.findViewById(R.id.fragment_cambio_text_valor_compra_moeda)
        textValorVendaMoeda = inflatedView.findViewById(R.id.fragment_cambio_text_valor_venda_moeda)
        textSaldoUsuario = inflatedView.findViewById(R.id.fragment_cambio_text_saldo_disponivel)
        textMoedasEmCaixa = inflatedView.findViewById(R.id.fragment_cambio_text_moeda_em_caixa)
        inputLayoutQuantidade =
            inflatedView.findViewById(R.id.fragment_cambio_input_layout_quantidade)
        buttonComprar = inflatedView.findViewById(R.id.fragment_cambio_button_comprar)
        buttonVender = inflatedView.findViewById(R.id.fragment_cambio_button_vender)
        inicializaViewModel()
    }

    private fun inicializaViewModel() {
        val usuarioDao = DatabaseBuilder(requireContext()).getDatabase().usuarioDao
        val factory = CambioViewModelFactory(usuarioDao)
        val provedor = ViewModelProvider(requireActivity(), factory)
        cambioViewModel = provedor.get(CambioViewModel::class.java)
    }

}
