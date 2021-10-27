package br.com.alexalves.investimentosbrq.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.alexalves.investimentosbrq.databinding.FragmentOperationSucessBinding
import br.com.alexalves.investimentosbrq.model.BusinessExchangeState
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.TypeOperation
import java.math.BigDecimal
import java.math.BigInteger

class OperationSucessFragment: Fragment() {

    lateinit var binding: FragmentOperationSucessBinding
    var buttonHomeListener: (()-> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOperationSucessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraButtonHome()
        configuraTextoSucesso()
    }

    private fun configuraTextoSucesso() {
        val operationSucess = arguments?.get("BusinessSucess") as BusinessExchangeState.Sucess
        binding.textOperacaoSucesso.text = operationSucess.message
    }

    private fun configuraButtonHome() {
        binding.operacaoSucedidaButtonHome.configuraTitulo("Home")
        configuraButtonHomeListener()
    }

    private fun configuraButtonHomeListener() {
        binding.operacaoSucedidaButtonHome.configuraClique = { buttonHomeListener?.invoke() }
    }
}