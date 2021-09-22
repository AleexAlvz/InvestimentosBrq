package br.com.alexalves.investimentosbrq.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.repository.MoedasRepository

class HomeViewModel(val usuarioDao: UsuarioDao) : ViewModel() {

    val moedasRepository = MoedasRepository(usuarioDao)

    fun buscaMoedas(
        quandoSucesso: ((moedas: List<Moeda>)->Unit)? = null,
        quandoFalha: ((erro: String)->Unit)? = null
    ){
        moedasRepository.configuraeFiltraMoedas(
            quandoSucesso = { moedas ->
            quandoSucesso?.invoke(moedas)
        }, quandoFalha = { erro ->
            quandoFalha?.invoke(erro)
        })
    }
}