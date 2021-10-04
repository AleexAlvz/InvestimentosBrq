package br.com.alexalves.investimentosbrq.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.repository.MoedasRepository

class HomeViewModel(
    val moedasRepository: MoedasRepository
) : ViewModel() {

    val listaDeMoedas = MutableLiveData<List<Moeda>>()

    fun buscaMoedas() {
        moedasRepository.buscaMoedas(
            quandoSucesso = { moedas ->
                listaDeMoedas.value = moedas
            }, quandoFalha = { erro ->
                Log.i("Busca Moedas", "Erro na busca de moedas: ${erro}")
            })
    }
}