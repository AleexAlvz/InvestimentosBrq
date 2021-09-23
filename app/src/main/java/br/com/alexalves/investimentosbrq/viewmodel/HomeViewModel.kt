package br.com.alexalves.investimentosbrq.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.repository.MoedasRepository

class HomeViewModel(val usuarioDao: UsuarioDao) : ViewModel() {

    val moedasRepository = MoedasRepository(usuarioDao)
    val listaDeMoedas = MutableLiveData<List<Moeda>>()

    fun buscaMoedas(){
        moedasRepository.configuraeFiltraMoedas(
            quandoSucesso = { moedas -> listaDeMoedas.value = moedas
        }, quandoFalha = { erro ->
           Log.i("Busca Moedas","Erro na busca de moedas: ${erro}")
        })
    }
}