package br.com.alexalves.investimentosbrq.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.TypeOperation
import br.com.alexalves.investimentosbrq.ui.customview.ButtonBlue
import java.math.BigDecimal
import java.math.BigInteger

class OperationSucessFragment: Fragment() {

    private lateinit var inflatedView: View
    private lateinit var textSucesso: TextView
    private lateinit var buttonHome: ButtonBlue
    var buttonHomeListener: (()-> Unit)? = null
    var quantidade: BigInteger = BigInteger.ZERO
    var valorTotal: BigDecimal = BigDecimal.ZERO
    var currency: Currency? = null
    var typeOperation: TypeOperation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflatedView = inflater.inflate(R.layout.fragment_operacao_sucedida, container, false)
        buscaCampos()
        configuraButtonHome()
        configuraTextoSucesso()
        return inflatedView
    }

    private fun configuraButtonHome() {
        buttonHome.configuraTitulo("Home")
        configuraButtonHomeListener()
    }

    private fun configuraTextoSucesso() {
        when(typeOperation){
            TypeOperation.PURCHASE -> { configuraTextoCompra() }
            TypeOperation.SALE -> { configuraTextoVenda() }
        }
    }

    private fun configuraTextoVenda() {
        val textoFormatado = "Parabéns! \n" +
                "Você acabou de vender ${quantidade} ${currency?.abbreviation} - ${currency?.name}, totalizando \n" +
                "R\$ ${valorTotal}"
        textSucesso.text = textoFormatado
    }

    private fun configuraTextoCompra() {
        val textoFormatado = "Parabéns! \n" +
                "Você acabou de comprar ${quantidade} ${currency?.abbreviation} - ${currency?.name}, totalizando \n" +
                "R\$ ${valorTotal}"
        textSucesso.text = textoFormatado
    }

    private fun buscaCampos() {
        textSucesso = inflatedView.findViewById(R.id.text_operacao_sucesso)
        buttonHome = inflatedView.findViewById(R.id.operacao_sucedida_button_home)
    }

    private fun configuraButtonHomeListener() {
        buttonHome.configuraClique = { buttonHomeListener?.invoke() }
    }
}