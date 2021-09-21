package br.com.alexalves.investimentosbrq

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.repository.MoedasRepository

class HomeViewModel: ViewModel() {

    val moedasRepository = MoedasRepository()

    fun buscaMoedas(
        quandoSucesso: ((moedas: List<Moeda>)->Unit)? = null,
        quandoFalha: ((erro: String)->Unit)? = null
    ){
        moedasRepository.buscaMoedas(
            quandoSucesso = { moedas ->
            quandoSucesso?.invoke(moedas)
        }, quandoFalha = { erro ->
            quandoFalha?.invoke(erro)
        })
    }
}