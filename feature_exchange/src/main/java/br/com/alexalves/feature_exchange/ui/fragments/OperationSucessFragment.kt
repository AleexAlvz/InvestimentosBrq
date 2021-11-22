package br.com.alexalves.feature_exchange.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.alexalves.base.BaseFragment
import br.com.alexalves.feature_exchange.databinding.FragmentOperationSucessBinding
import br.com.alexalves.models.BusinessExchangeState
import br.com.alexalves.models.consts.ArgumentConsts
import br.com.alexalves.models.consts.OperationConsts
import br.com.alexalves.models.consts.TextsConsts
import org.w3c.dom.Text

class OperationSucessFragment: BaseFragment() {

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
        val operationSucess = arguments?.get(ArgumentConsts.business_sucess_argument) as BusinessExchangeState.Sucess
        binding.textOperacaoSucesso.text = operationSucess.message
    }

    private fun configuraButtonHome() {
        binding.operacaoSucedidaButtonHome.configuraTitulo(TextsConsts.TextHome)
        configuraButtonHomeListener()
    }

    private fun configuraButtonHomeListener() {
        binding.operacaoSucedidaButtonHome.configuraClique = { buttonHomeListener?.invoke() }
    }
}