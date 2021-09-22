package br.com.alexalves.investimentosbrq.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.database.DatabaseBuilder
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.viewmodel.CambioViewModel
import br.com.alexalves.investimentosbrq.viewmodel.viewModelFactory.CambioViewModelFactory

class CambioFragment : Fragment() {

    private lateinit var inflatedView: View
    private lateinit var moeda: Moeda
    private lateinit var textTituloMoeda: TextView
    private lateinit var textVariacaoMoeda: TextView
    private lateinit var textValorCompraMoeda: TextView
    private lateinit var textValorVendaMoeda: TextView
    private lateinit var textSaldoUsuario: TextView
    private lateinit var textMoedasEmCaixa: TextView
    lateinit var cambioViewModel: CambioViewModel
    var moedasEmCaixa: Int? = null

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

    private fun buscaDadosUsuario() {
        cambioViewModel.atualizaSaldo()
    }

    private fun observaUsuario() {
        observaSaldoUsuario()
//        observaMoedasEmCaixaUsuario()
    }

    private fun observaSaldoUsuario() {
        cambioViewModel.saldoUsuario.observe(viewLifecycleOwner, Observer { saldo ->
            if (saldo!=null){
                val saldoFormatado = "Saldo disponível: R$ ${saldo.toString().replace(".",",")}"
                textSaldoUsuario.text = saldoFormatado
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
        val moedasEmCaixaFormatado = "0 ${moeda.abreviacao} em caixa"
        textMoedasEmCaixa.text = moedasEmCaixaFormatado
    }

    private fun setVariacaoColor() {
        if (moeda.variation>0){
            textVariacaoMoeda.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_positive))
        } else if (moeda.variation<0){
            textVariacaoMoeda.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_negative))
        } else textVariacaoMoeda.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun inicializaCampos() {
        //Busca moeda no bundle do fragment
        moeda = arguments?.get(getString(R.string.moeda_argument)) as Moeda
        //busca textViews do Card Moeda
        textTituloMoeda = inflatedView.findViewById(R.id.fragment_cambio_text_titulo_moeda)
        textVariacaoMoeda = inflatedView.findViewById(R.id.fragment_cambio_text_variacao_moeda)
        textValorCompraMoeda = inflatedView.findViewById(R.id.fragment_cambio_text_valor_compra_moeda)
        textValorVendaMoeda = inflatedView.findViewById(R.id.fragment_cambio_text_valor_venda_moeda)
        textSaldoUsuario = inflatedView.findViewById(R.id.fragment_cambio_text_saldo_disponivel)
        textMoedasEmCaixa = inflatedView.findViewById(R.id.fragment_cambio_text_moeda_em_caixa)
        inicializaViewModel()
    }

    private fun inicializaViewModel() {
        val usuarioDao = DatabaseBuilder(requireContext()).getDatabase().usuarioDao
        val factory = CambioViewModelFactory(usuarioDao)
        val provedor = ViewModelProvider(requireActivity(), factory)
        cambioViewModel = provedor.get(CambioViewModel::class.java)
    }

}
