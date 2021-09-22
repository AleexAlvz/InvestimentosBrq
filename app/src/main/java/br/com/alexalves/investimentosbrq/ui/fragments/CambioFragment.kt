package br.com.alexalves.investimentosbrq.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Moeda

class CambioFragment : Fragment() {

    private lateinit var inflatedView: View
    private lateinit var moeda: Moeda
    private lateinit var textTituloMoeda: TextView
    private lateinit var textVariacaoMoeda: TextView
    private lateinit var textValorCompraMoeda: TextView
    private lateinit var textValorVendaMoeda: TextView
    private lateinit var textSaldoUsuario: TextView
    private lateinit var textMoedasEmCaixa: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflatedView = inflater.inflate(R.layout.fragment_cambio, container, false)
        inicializaCampos()
        configuraTextoNosCampos()
        return inflatedView
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
        textMoedasEmCaixa = inflatedView.findViewById(R.id.fragment_cambio_text_moeda_em_caixa
        )

    }

}
